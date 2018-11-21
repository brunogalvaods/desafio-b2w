package br.com.b2w.desafio.converter;

import java.util.ArrayList;
import java.util.List;

import br.com.b2w.desafio.model.Planeta;
import br.com.b2w.desafio.vo.PlanetaVo;

public class PlanetaConverter {

	public static List<PlanetaVo> listarPlanetas(List<Planeta> planetas) {
		List<PlanetaVo> planetaVos = new ArrayList<PlanetaVo>();

		for (Planeta planeta : planetas) {
			planetaVos.add(planetaToPlanetaVo(planeta));
		}

		return planetaVos;
	}

	public static PlanetaVo planetaToPlanetaVo(final Planeta planeta) {
		PlanetaVo planetaVo = null;

		if (planeta != null) {
			planetaVo = new PlanetaVo();

			planetaVo.setId(planeta.getId());
			planetaVo.setNome(planeta.getNome());
			planetaVo.setClima(planeta.getClima());
			planetaVo.setTerreno(planeta.getTerreno());
			planetaVo.setAparicoes(planeta.getAparicoes());
		}

		return planetaVo;
	}

	public static Planeta planetaVoToSave(PlanetaVo planetaVo, Planeta planeta) {
		if (planetaVo != null) {
			if (planeta == null) {
				planeta = new Planeta();
			}
			planeta.setNome(planetaVo.getNome());
			planeta.setClima(planetaVo.getClima());
			planeta.setTerreno(planetaVo.getTerreno());
			planeta.setAparicoes(planetaVo.getAparicoes());
		}

		return planeta;
	}

}
