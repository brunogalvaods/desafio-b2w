package br.com.b2w.desafio.api;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.b2w.desafio.model.Planeta;
import br.com.b2w.desafio.service.PlanetaService;
import br.com.b2w.desafio.util.TestUtil;
import br.com.b2w.desafio.vo.PlanetaVo;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class PlanetaApiTests {

	private MockMvc mockMvc;

	@InjectMocks
	private PlanetaApi planetaApiMock;

	@Mock
	private PlanetaService planetaServiceMock;

	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(planetaApiMock).build();
	}

	@Test
	public void listarSuccessTest() throws Exception {
		PlanetaVo p1 = new PlanetaVo();
		p1.setId("5bf4161bd07bc8237ec34562");
		p1.setNome("Alderaan");
		p1.setClima("temperate");
		p1.setTerreno("grasslands, mountains");

		PlanetaVo p2 = new PlanetaVo();
		p2.setId("5bf41679d07bc8237ec34563");
		p2.setNome("Dagobah");
		p2.setClima("murky");
		p2.setTerreno("swamp, jungles");

		List<PlanetaVo> planetas = new ArrayList<PlanetaVo>(Arrays.asList(p1, p2));

		PageRequest pageRequest = new PageRequest(0, 10);
		Page<PlanetaVo> planetasPage = new PageImpl<PlanetaVo>(planetas, pageRequest, planetas.size());

		when(planetaServiceMock.listar(null, pageRequest)).thenReturn(planetasPage);

		mockMvc.perform(get("/api/planetas")).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.content[0].id", is("5bf4161bd07bc8237ec34562")))
				.andExpect(jsonPath("$.content[0].nome", is("Alderaan")))
				.andExpect(jsonPath("$.content[1].id", is("5bf41679d07bc8237ec34563")))
				.andExpect(jsonPath("$.content[1].nome", is("Dagobah")));

		verify(planetaServiceMock, times(1)).listar(null, pageRequest);
		verifyNoMoreInteractions(planetaServiceMock);
	}

	@Test
	public void listarNoContentTest() throws Exception {
		List<PlanetaVo> planetas = new ArrayList<PlanetaVo>(Arrays.asList());

		PageRequest pageRequest = new PageRequest(0, 10);
		Page<PlanetaVo> planetasPage = new PageImpl<PlanetaVo>(planetas, pageRequest, planetas.size());

		when(planetaServiceMock.listar(null, pageRequest)).thenReturn(planetasPage);

		mockMvc.perform(get("/api/planetas")).andExpect(status().isNoContent());

		verify(planetaServiceMock, times(1)).listar(null, pageRequest);
		verifyNoMoreInteractions(planetaServiceMock);
	}

	@Test
	public void salvarSuccessTest() throws Exception {
		Planeta planeta = new Planeta();
		planeta.setNome("Alderaan");
		planeta.setClima("temperate");
		planeta.setTerreno("grasslands, mountains");

		Planeta planetaMock = new Planeta();
		planetaMock.setId("5bf4161bd07bc8237ec34562");
		planetaMock.setNome("Alderaan");
		planetaMock.setClima("temperate");
		planetaMock.setTerreno("grasslands, mountains");

		when(planetaServiceMock.salvar(planeta)).thenReturn(planetaMock);

		mockMvc.perform(post("/api/planetas").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(planeta))).andExpect(status().isCreated());
	}

	@Test
	public void buscarPorIdSuccessTest() throws Exception {
		Planeta planetaMock = new Planeta();
		planetaMock.setId("5bf4161bd07bc8237ec34562");
		planetaMock.setNome("Alderaan");
		planetaMock.setClima("temperate");
		planetaMock.setTerreno("grasslands, mountains");

		when(planetaServiceMock.buscarPorId("5bf4161bd07bc8237ec34562")).thenReturn(Optional.of(planetaMock));
		
		mockMvc.perform(get("/api/planetas/{id}", "5bf4161bd07bc8237ec34562"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is("5bf4161bd07bc8237ec34562")))
				.andExpect(jsonPath("$.nome", is("Alderaan")));

		verify(planetaServiceMock, times(1)).buscarPorId("5bf4161bd07bc8237ec34562");
		verifyNoMoreInteractions(planetaServiceMock);
	}
	
	@Test
	public void deletarSuccessTest() throws Exception {
		Planeta planetaMock = new Planeta();
		planetaMock.setId("5bf4161bd07bc8237ec34562");
		planetaMock.setNome("Alderaan");
		planetaMock.setClima("temperate");
		planetaMock.setTerreno("grasslands, mountains");

		when(planetaServiceMock.buscarPorId("5bf4161bd07bc8237ec34562")).thenReturn(Optional.of(planetaMock));
		doNothing().when(planetaServiceMock).deletar(planetaMock);
		
		mockMvc.perform(delete("/api/planetas/{id}", "5bf4161bd07bc8237ec34562"))
				.andExpect(status().isNoContent());

		verify(planetaServiceMock, times(1)).buscarPorId(planetaMock.getId());
	    verify(planetaServiceMock, times(1)).deletar(planetaMock);
	    verifyNoMoreInteractions(planetaServiceMock);
	}

}
