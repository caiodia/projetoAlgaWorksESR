package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@RestController
@RequestMapping("/teste")
public class TesteController {

	@Autowired
	private CozinhaRepository cozinhaRepository;

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	//=== Cozinha
	
	@GetMapping("/cozinhas/por-nome")
	public List<Cozinha> cozinhasPorNome(@RequestParam("nome") String nome){
		return cozinhaRepository.findTodasByNome(nome);
	}
	
	@GetMapping("/cozinhas/por-parte-nome")
	public List<Cozinha> cozinhasPorParteNome(@RequestParam("nome") String nome){
		return cozinhaRepository.findTodasByNomeContaining(nome);
	}
	
	@GetMapping("/cozinhas/unica-por-nome")
	public Optional<Cozinha> cozinhaPorNome(@RequestParam("nome") String nome){
		return cozinhaRepository.findByNome(nome);
	}
	
	@GetMapping("/cozinhas/exists")
	public boolean cozinhaExists(String nome){
		return cozinhaRepository.existsByNome(nome);
	}
	
	//=== Restaurante
	/*
	 * @GetMapping("/restaurantes/por-taxa-frete") public List<Restaurante>
	 * restaurantePorTaxaFrete( BigDecimal taxaInicial, BigDecimal taxaFinal){
	 * return restauranteRepository.findByTaxaFreteBetween(taxaInicial, taxaFinal);
	 * }
	 */
	
	/*
	 * @GetMapping("/restaurantes/por-nome") public List<Restaurante>
	 * restaurantesPorNome( String nome, Long cozinhaId) { return
	 * restauranteRepository.findByNomeContainingAndCozinhaId(nome, cozinhaId); }
	 */
	
	@GetMapping("/restaurantes/por-nome")
	public List<Restaurante> restaurantesPorNome(
			String nome, Long cozinhaId) {
		return restauranteRepository.consultarPorNome(nome, cozinhaId);
	}
	
	@GetMapping("/restaurantes/primeiro-por-nome")
	public Optional<Restaurante> restaurantesPorNome(
			String nome) {
		return restauranteRepository.findFirstRestauranteByNomeContaining(nome);
	}
	
	@GetMapping("/restaurantes/top2-por-nome")
	public List<Restaurante> restaurantesPorNomeTop2(
			String nome) {
		return restauranteRepository.findTop2ByNomeContaining(nome);
	}
	
	@GetMapping("/restaurantes/count-por-cozinha")
	public int restaurantesCountPorCozinha(Long cozinhaId) {
		return restauranteRepository.countByCozinhaId(cozinhaId);
	}
}
