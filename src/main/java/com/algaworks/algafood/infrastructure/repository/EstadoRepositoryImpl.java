package com.algaworks.algafood.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;

@Component
public class EstadoRepositoryImpl implements EstadoRepository{

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<Estado> listar() {
		
		//JPQL - Linguagem de consulta do JPA
		return manager.createQuery("from Estado", Estado.class).getResultList();
	}

	@Override
	public Estado buscar(Long id) {
		return manager.find(Estado.class, id);
	}
	
	//adicionando um objeto
	@Transactional
	@Override
	public Estado salvar(Estado Estado) {
		//insert into Estado (nome) values ()
		return manager.merge(Estado);
	}
	
	@Transactional
	@Override
	public void remover(Estado Estado) {
		// Transient -> Managed -> remove
		// da maneira que a instancia chega ela está na etapa de 'Transient', sendo necessário fazer uma busca pelo ID para que ela passe para 
		// a etapa de 'Managed', só então conseguiremos fazer o remove e excluir o registro.
		Estado = buscar(Estado.getId());
		manager.remove(Estado);
	}
}
