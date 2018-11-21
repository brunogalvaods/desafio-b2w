package br.com.b2w.desafio.api;

import java.util.Optional;

import javax.validation.Valid;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import br.com.b2w.desafio.converter.PlanetaConverter;
import br.com.b2w.desafio.exception.PlanetNotFoundException;
import br.com.b2w.desafio.model.Planeta;
import br.com.b2w.desafio.service.PlanetaService;
import br.com.b2w.desafio.vo.PlanetaVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Classe responsável por executar as operações relacionadas aos Planetas.
 * 
 * @author Bruno Galvão
 *
 */
@Api(description = "API para efetuar as operações dos planetas")
@RestController
@RequestMapping("/api/planetas")
public class PlanetaApi {

	private transient static Logger logger = LoggerFactory.getLogger(PlanetaApi.class);

	@Autowired
	private transient PlanetaService service;

	@ApiOperation(value = "Salva o planeta na base de dados", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Registro salvo com sucesso!"),
			@ApiResponse(code = 400, message = "O registro que você estava tentando salvar está inválido."),
			@ApiResponse(code = 500, message = "Ocorreu um erro inesperado, contate o administrador.") })
	@RequestMapping(method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<?> salvar(@Valid @RequestBody final PlanetaVo planetaVo) {
		try {
			getAparicoesFilme(planetaVo);
			Planeta planeta = PlanetaConverter.planetaVoToSave(planetaVo, null);
			service.salvar(planeta);

			return new ResponseEntity<Planeta>(planeta, HttpStatus.CREATED);
		} catch (final PlanetNotFoundException pnfe) {
			return new ResponseEntity<Object>(pnfe.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (final Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Método responsável por recuperar a quantidade de aparições por filme.
	 * 
	 * @param planetaVo
	 * @throws UnirestException 
	 */
	private void getAparicoesFilme(final PlanetaVo planetaVo) throws PlanetNotFoundException, UnirestException {
		String string = "https://swapi.co/api/planets/?search=";
		HttpResponse<JsonNode> response = Unirest
				.get(string.concat(planetaVo.getNome())).asJson();
		if (response.getStatus() == 200) {
			Integer count = Integer.parseInt(response.getBody().getObject().get("count").toString());
			if (count > 0) {
				JSONArray results = (JSONArray) response.getBody().getObject().get("results");
				JSONObject films = (JSONObject) results.get(0);
				JSONArray array = (JSONArray) films.get("films");
				planetaVo.setAparicoes(array.length());
			} else {
				throw new PlanetNotFoundException("Nãof foi encontrado o planeta no catálogo.");
			}
		}

	}

	/**
	 * Método responsável por listar todos os Planetas.
	 * 
	 * @param page     - número da página
	 * @param pageSize - quantidade de registro por página
	 * @return
	 */
	@ApiOperation(value = "Lista com todos os planetas disponíveis", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lista retornada com sucesso!"),
			@ApiResponse(code = 204, message = "Não existe nenhum registro disponível para ser exibido."),
			@ApiResponse(code = 404, message = "O recurso que você estava tentando recuperar não foi encontrado."),
			@ApiResponse(code = 500, message = "Ocorreu um erro inesperado, contate o administrador.") })
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> listar(@RequestParam(value = "page", defaultValue = "0") final Integer page,
			@RequestParam(value = "size", defaultValue = "10") final Integer pageSize,
			@RequestParam(value = "nome", required = false) String nome) {
		try {
			PageRequest pageRequest = new PageRequest(page, pageSize);

			Page<PlanetaVo> planetas = service.listar(nome, pageRequest);
			if (planetas != null && planetas.getTotalElements() > 0) {
				return new ResponseEntity<Page<PlanetaVo>>(planetas, HttpStatus.OK);
			}
			return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		} catch (final Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Busca o planeta na base de dados", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Registro retornardo com sucesso!"),
			@ApiResponse(code = 404, message = "O recurso que você estava tentando recuperar não foi encontrado."),
			@ApiResponse(code = 500, message = "Ocorreu um erro inesperado, contate o administrador.") })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> buscar(@PathVariable("id") String id) {
		try {
			Optional<Planeta> planeta = service.buscarPorId(id);
			if (planeta != null) {
				PlanetaVo planetaVo = PlanetaConverter.planetaToPlanetaVo(planeta.get());
				return new ResponseEntity<PlanetaVo>(planetaVo, HttpStatus.OK);
			} else {
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			}
		} catch (final Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Exclui o planeta na base de dados", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Registro excluído com sucesso!"),
			@ApiResponse(code = 404, message = "O recurso que você estava tentando recuperar não foi encontrado."),
			@ApiResponse(code = 500, message = "Ocorreu um erro inesperado, contate o administrador.") })
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> deletar(@PathVariable("id") String id) {
		try {

			Optional<Planeta> planetaBase = service.buscarPorId(id);

			if (planetaBase == null) {
				return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
			} else {
				service.deletar(planetaBase.get());
			}

			return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		} catch (final Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
