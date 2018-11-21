package br.com.b2w.desafio.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.b2w.desafio.model.Planeta;

public interface PlanetaRepository extends MongoRepository<Planeta, String> {

	Page<Planeta> findAll(Pageable pageable);
	
	Page<Planeta> findByNome(String nome, Pageable pageable);
	
}
