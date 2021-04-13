package com.algaworks.algafood.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@Component
public class CozinhaRepositoryImpl implements CozinhaRepository{

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<Cozinha> listar() {
		
		//JPQL - Linguagem de consulta do JPA
		return manager.createQuery("from Cozinha", Cozinha.class).getResultList();
	}

	@Override
	public Cozinha buscar(Long id) {
		return manager.find(Cozinha.class, id);
	}
	
	//adicionando um objeto
	@Transactional
	@Override
	public Cozinha salvar(Cozinha cozinha) {
		//insert into cozinha (nome) values ()
		return manager.merge(cozinha);
	}
	
	@Transactional
	@Override
	public void remover(Cozinha cozinha) {
		// Transient -> Managed -> remove
		// da maneira que a instancia chega ela está na etapa de 'Transient', sendo necessário fazer uma busca pelo ID para que ela passe para 
		// a etapa de 'Managed', só então conseguiremos fazer o remove e excluir o registro.
		cozinha = buscar(cozinha.getId());
		manager.remove(cozinha);
	}
}
