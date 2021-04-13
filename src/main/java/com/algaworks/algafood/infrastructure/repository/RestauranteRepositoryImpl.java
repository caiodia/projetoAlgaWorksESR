package com.algaworks.algafood.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Component
public class RestauranteRepositoryImpl implements RestauranteRepository{

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<Restaurante> listar() {
		
		//JPQL - Linguagem de consulta do JPA
		return manager.createQuery("from Restaurante", Restaurante.class).getResultList();
	}

	@Override
	public Restaurante buscar(Long id) {
		return manager.find(Restaurante.class, id);
	}
	
	//adicionando um objeto
	@Transactional
	@Override
	public Restaurante salvar(Restaurante restaurante) {
		//insert into cozinha (nome) values ()
		return manager.merge(restaurante);
	}
	
	@Transactional
	@Override
	public void remover(Restaurante restaurante) {
		// Transient -> Managed -> remove
		// da maneira que a instancia chega ela está na etapa de 'Transient', sendo necessário fazer uma busca pelo ID para que ela passe para 
		// a etapa de 'Managed', só então conseguiremos fazer o remove e excluir o registro.
		restaurante = buscar(restaurante.getId());
		manager.remove(restaurante);
	}
}
