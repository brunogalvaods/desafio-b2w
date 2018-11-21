package br.com.b2w.desafio.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.b2w.desafio.model.Planeta;
import br.com.b2w.desafio.vo.PlanetaVo;

public interface PlanetaService {

	Planeta salvar(Planeta planeta);
	
	Page<PlanetaVo> listar(String nome, Pageable pageable);

	Page<Planeta> buscarPorNome(String nome, Pageable pageable);
	
	Optional<Planeta> buscarPorId(String id);
	
	void deletar(Planeta planeta);
	
}
