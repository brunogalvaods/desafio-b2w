package br.com.b2w.desafio.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.b2w.desafio.model.Planeta;
import br.com.b2w.desafio.repository.PlanetaRepository;
import br.com.b2w.desafio.service.impl.PlanetaServiceImpl;
import br.com.b2w.desafio.vo.PlanetaVo;

@RunWith(SpringRunner.class)
public class PlanetaServiceTests {

	@TestConfiguration
	static class planetaServiceImplTestContextConfiguration {
		@Bean
		public PlanetaService planetaService() {
			return new PlanetaServiceImpl();
		}
	}
	
	@Autowired
	private PlanetaService planetaService;

	@MockBean
	private PlanetaRepository planetaRepositoryMock;
	
	@Before
	public void setUp() {
		Planeta planeta = new Planeta();
		
		planeta.setId("5bf4161bd07bc8237ec34562");
		planeta.setNome("Alderaan");
		planeta.setClima("temperate");
		planeta.setTerreno("grasslands, mountains");
		Optional<Planeta> planetaOptional = Optional.of(planeta);
		
		List<Planeta> planetas = Arrays.asList(planeta);
		
		PageRequest pageRequest = new PageRequest(0, 10);
		Page<Planeta> planetasPage = new PageImpl<Planeta>(planetas, pageRequest, planetas.size());
		
		when(planetaRepositoryMock.findAll(pageRequest)).thenReturn(planetasPage);
		
		when(planetaRepositoryMock.findById(planetaOptional.get().getId())).thenReturn(planetaOptional);
	}

	@Test
	public void salvar() {
		Planeta planeta = new Planeta();
		planeta.setId("5bf4161bd07bc8237ec34562");
		planeta.setNome("Alderaan");
		planeta.setClima("temperate");
		planeta.setTerreno("grasslands, mountains");

		when(planetaRepositoryMock.save(planeta)).thenReturn(planeta);
		
		Planeta retorno = planetaService.salvar(planeta);

		assertEquals(retorno.getId(), "5bf4161bd07bc8237ec34562");
	}
	
	/**
	 * Método responsável por realizar o teste para listar os planetas.
	 */
	@Test
	public void listar() {
		Planeta p1 = new Planeta();
		p1.setId("5bf4161bd07bc8237ec34562");
		
		PageRequest pageRequest = new PageRequest(0, 10);
		Page<PlanetaVo> planetas = planetaService.listar(null, pageRequest);

		assertEquals(planetas.getContent().get(0).getId(), p1.getId());
	}
	
	@Test
	public void buscarPorId() {
		Planeta p1 = new Planeta();
		p1.setId("5bf4161bd07bc8237ec34562");
		
		Optional<Planeta> planeta = planetaService.buscarPorId("5bf4161bd07bc8237ec34562");

		assertEquals(planeta.get().getId(), p1.getId());
	}
	
}
