package br.com.b2w.desafio.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.b2w.desafio.converter.PlanetaConverter;
import br.com.b2w.desafio.model.Planeta;
import br.com.b2w.desafio.repository.PlanetaRepository;
import br.com.b2w.desafio.service.PlanetaService;
import br.com.b2w.desafio.vo.PlanetaVo;

@Service
public class PlanetaServiceImpl implements PlanetaService {

	@Autowired
	private transient PlanetaRepository repository;
	

	@Override
	public Planeta salvar(final Planeta planeta) {
		return repository.save(planeta);
	}

	@Override
	public Page<PlanetaVo> listar(final String nome, final Pageable pageable) {
		Page<Planeta> planetasPageInfo = null;
		if(nome != null) {
			planetasPageInfo =  repository.findByNome(nome, pageable);
		} else {
			planetasPageInfo = repository.findAll(pageable);
		}
		List<Planeta> planetas = planetasPageInfo.getContent();
		
		return new PageImpl<PlanetaVo>(PlanetaConverter.listarPlanetas(planetas), pageable, planetasPageInfo.getTotalElements());
	}
	
	@Override
	public Page<Planeta> buscarPorNome(final String nome, final Pageable pageable) {
		return repository.findByNome(nome, pageable);
	}

	@Override
	public Optional<Planeta> buscarPorId(String id) {
		return repository.findById(id);
	}

	@Override
	public void deletar(final Planeta planeta) {
		repository.delete(planeta);
	}
	
}
