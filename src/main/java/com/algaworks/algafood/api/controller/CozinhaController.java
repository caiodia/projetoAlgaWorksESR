package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

//@Controller  -> está dentro da anotação @RestController
//@ResponseBody  -> está dentro da anotação @RestController
@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@GetMapping
	public List<Cozinha> listar(){
		return cozinhaRepository.listar();
	}
	
	@GetMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {
		//return cozinhaRepository.buscar(cozinhaId);
		
		Cozinha cozinha = cozinhaRepository.buscar(cozinhaId);
		
		//return ResponseEntity.status(HttpStatus.OK).build(); // Devolve a requisição com 200 mas sem body
		//return ResponseEntity.status(HttpStatus.OK).body(cozinha); // Devolve 200 com body
		//return ResponseEntity.ok(cozinha); // idem ao de cima mas simplificado
		
		/*
		 * HttpHeaders headers = new HttpHeaders(); 
		 * headers.add(HttpHeaders.LOCATION, "http://api.algafood.local:8080/cozinhas");
		 * return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
		 */
		
		if (cozinha != null) {
			return ResponseEntity.ok(cozinha);
		}
		
		//return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void adicionar(@RequestBody Cozinha cozinha){
		cozinhaRepository.salvar(cozinha);
	}
	
	@PutMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> atualizar(@RequestBody Cozinha cozinha, 
			@PathVariable Long cozinhaId){
		
			Cozinha cozinhaAtual = cozinhaRepository.buscar(cozinhaId);
			
			if (cozinhaAtual != null) {
				// Origem - Destino - Propriedades para desprezar
				BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");
				
				cozinhaRepository.salvar(cozinhaAtual);
				return ResponseEntity.ok(cozinhaAtual);
			}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> remover(@PathVariable Long cozinhaId){
		
		try {
			Cozinha cozinha = cozinhaRepository.buscar(cozinhaId);
			
			if(cozinha != null) {
				cozinhaRepository.remover(cozinha);
				return ResponseEntity.noContent().build();
			}
			
			return ResponseEntity.notFound().build();			
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		
	}
}