package com.algaworks.algafood.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Cozinha;

//esta anotação cria uma implementação de repositório
@Repository
public interface CozinhaRepository extends JpaRepository<Cozinha, Long>{
	
	//retorna uma lista
		List<Cozinha> findTodasByNome(String nome);
			
	//retorna um unico elemento
		Optional<Cozinha> findByNome(String nome);	
		
	//retorna uma lista que contenha a string passada como parametro
		List<Cozinha> findTodasByNomeContaining(String nome);
		
		boolean existsByNome(String nome);
}
