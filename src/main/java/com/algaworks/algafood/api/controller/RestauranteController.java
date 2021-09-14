package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.RestauranteInputDisassembler;
import com.algaworks.algafood.api.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.api.model.view.RestauranteView;
import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	@Autowired
	private RestauranteModelAssembler restauranteModelAssembler;
	
	@Autowired
	private RestauranteInputDisassembler restauranteInputDisassembler;
		
	/* versao 1
	 * 
	 * @GetMapping public List<Restaurante> listar(){ return
	 * restauranteRepository.findAll(); }
	 */
	
	@JsonView(RestauranteView.Resumo.class)
	@GetMapping
	public List<RestauranteModel> listar() {
		return restauranteModelAssembler.toCollectionModel(restauranteRepository.findAll());
	}
	
	@JsonView(RestauranteView.ApenasNome.class)
	@GetMapping(params = "projecao=apenas-nome")
	public List<RestauranteModel> listarApenasNomes() {
		return listar();
	}
	
	/* Versao 1 com uso do optional
	 * 
	 * @GetMapping("/{restauranteId}") public ResponseEntity<Restaurante>
	 * buscar(@PathVariable Long restauranteId){ Optional<Restaurante> restaurante =
	 * restauranteRepository.findById(restauranteId);
	 * 
	 * if (restaurante.isPresent()) { return ResponseEntity.ok(restaurante.get()); }
	 * 
	 * return ResponseEntity.notFound().build(); }
	 */
	
	/* versao 2
	 * @GetMapping("/{restauranteId}") public Restaurante buscar(@PathVariable Long
	 * restauranteId) { return cadastroRestaurante.buscarOuFalhar(restauranteId); }
	 */
	
	@GetMapping("/{restauranteId}")
	public RestauranteModel buscar(@PathVariable Long restauranteId) {
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);
		
		return restauranteModelAssembler.toModel(restaurante);
	}
	
	/* Versao 1
	 * 
	 * @PostMapping public ResponseEntity<?> adicionar(@RequestBody Restaurante
	 * restaurante) { try { restaurante = cadastroRestaurante.salvar(restaurante);
	 * 
	 * return ResponseEntity.status(HttpStatus.CREATED) .body(restaurante); } catch
	 * (EntidadeNaoEncontradaException e) { return
	 * ResponseEntity.badRequest().body(e.getMessage()); }
	 * 
	 * }
	 */
	
	/* versao 2
	 * 
	 * @PostMapping
	 * 
	 * @ResponseStatus(HttpStatus.CREATED) public Restaurante adicionar(
	 * 
	 * @RequestBody @Valid Restaurante restaurante) { try { return
	 * cadastroRestaurante.salvar(restaurante); } catch
	 * (CozinhaNaoEncontradaException e) { throw new
	 * NegocioException(e.getMessage()); } }
	 */
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteModel adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {
		try {
			Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);
			
			return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restaurante));
		} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}
	
	/* Versao 1
	 * 
	 * @PutMapping("/{restauranteId}") public ResponseEntity<?>
	 * atualizar(@PathVariable Long restauranteId,
	 * 
	 * @RequestBody Restaurante restaurante){ try { Optional<Restaurante>
	 * restauranteAtual = restauranteRepository.findById(restauranteId);
	 * 
	 * // copia as propriedades exceto o id e o campo formasPagamento para não
	 * sobrescrever. if(restauranteAtual.isPresent()) {
	 * BeanUtils.copyProperties(restaurante, restauranteAtual.get(), "id",
	 * "formasPagamento", "endereco");
	 * 
	 * Restaurante restauranteSalvo =
	 * cadastroRestaurante.salvar(restauranteAtual.get()); return
	 * ResponseEntity.ok(restauranteSalvo); }
	 * 
	 * return ResponseEntity.notFound().build();
	 * 
	 * } catch (EntidadeNaoEncontradaException e) { return
	 * ResponseEntity.badRequest().body(e.getMessage()); } }
	 */
	
	/* versao 2
	 * 
	 * @PutMapping("/{restauranteId}") public Restaurante atualizar(@PathVariable
	 * Long restauranteId,
	 * 
	 * @RequestBody Restaurante restaurante) { try { Restaurante restauranteAtual =
	 * cadastroRestaurante.buscarOuFalhar(restauranteId);
	 * 
	 * BeanUtils.copyProperties(restaurante, restauranteAtual, "id",
	 * "formasPagamento", "endereco", "dataCadastro", "produtos");
	 * 
	 * return cadastroRestaurante.salvar(restauranteAtual); } catch
	 * (CozinhaNaoEncontradaException e) { throw new
	 * NegocioException(e.getMessage()); } }
	 */
	
	@PutMapping("/{restauranteId}")
	public RestauranteModel atualizar(@PathVariable Long restauranteId,
			@RequestBody @Valid RestauranteInput restauranteInput) {
		try {
			Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);
			
			restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);
			
			return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restauranteAtual));
		} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}
	
	/* Versao 1
	 * 
	 * @PatchMapping("/{restauranteId}") public ResponseEntity<?>
	 * atualizarParcial(@PathVariable Long restauranteId,
	 * 
	 * @RequestBody Map<String, Object> campos){
	 * 
	 * Optional<Restaurante> restauranteAtual =
	 * restauranteRepository.findById(restauranteId);
	 * 
	 * if(restauranteAtual.isEmpty()) { return ResponseEntity.notFound().build(); }
	 * 
	 * merge(campos, restauranteAtual.get());
	 * 
	 * return atualizar(restauranteId, restauranteAtual.get()); }
	 */
	
	/* versao 1
	 * @PatchMapping("/{restauranteId}") public Restaurante
	 * atualizarParcial(@PathVariable Long restauranteId,
	 * 
	 * @RequestBody Map<String, Object> campos, HttpServletRequest request) {
	 * Restaurante restauranteAtual =
	 * cadastroRestaurante.buscarOuFalhar(restauranteId);
	 * 
	 * merge(campos, restauranteAtual, request);
	 * 
	 * return atualizar(restauranteId, restauranteAtual); }
	 */
	
	/*
	 * private void merge(Map<String, Object> dadosOrigem, Restaurante
	 * restauranteDestino) { ObjectMapper objectMapper = new ObjectMapper();
	 * 
	 * //Vindo da requisição - Via postman ( objectMapper ja convert considerando os
	 * types definidos no objeto informado Restaurante restauranteOrigem =
	 * objectMapper.convertValue(dadosOrigem, Restaurante.class);
	 * 
	 * dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> { Field field =
	 * ReflectionUtils.findField(Restaurante.class, nomePropriedade); // Habilita
	 * acesso de atributos privados da classe Restaurante field.setAccessible(true);
	 * 
	 * // Pega o valor do campo vindo na requisição Object novoValor =
	 * ReflectionUtils.getField(field, restauranteOrigem);
	 * 
	 * // System.out.println(nomePropriedade + " = " + valorPropriedade + " = " +
	 * novoValor);
	 * 
	 * ReflectionUtils.setField(field, restauranteDestino, novoValor);
	 * 
	 * System.out.println(nomePropriedade + " = " + valorPropriedade + " = " +
	 * novoValor); }); }
	 */
	
	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long restauranteId) {
		cadastroRestaurante.ativar(restauranteId);
	}
	
	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativar(@PathVariable Long restauranteId) {
		cadastroRestaurante.inativar(restauranteId);
	}
	
	@PutMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativarMultiplos(@RequestBody List<Long> restauranteIds) {
		try {
			cadastroRestaurante.ativar(restauranteIds);
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@DeleteMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativarMultiplos(@RequestBody List<Long> restauranteIds) {
		try {
			cadastroRestaurante.inativar(restauranteIds);
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@PutMapping("/{restauranteId}/abertura")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void abrir(@PathVariable Long restauranteId) {
		cadastroRestaurante.abrir(restauranteId);
	}
	
	@PutMapping("/{restauranteId}/fechamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void fechar(@PathVariable Long restauranteId) {
		cadastroRestaurante.fechar(restauranteId);
	}

	/*
	 * private void merge(Map<String, Object> dadosOrigem, Restaurante
	 * restauranteDestino, HttpServletRequest request) { ServletServerHttpRequest
	 * serverHttpRequest = new ServletServerHttpRequest(request);
	 * 
	 * try { ObjectMapper objectMapper = new ObjectMapper();
	 * objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
	 * true);
	 * objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
	 * true);
	 * 
	 * Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem,
	 * Restaurante.class);
	 * 
	 * dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> { Field field =
	 * ReflectionUtils.findField(Restaurante.class, nomePropriedade);
	 * field.setAccessible(true);
	 * 
	 * Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
	 * 
	 * ReflectionUtils.setField(field, restauranteDestino, novoValor); }); } catch
	 * (IllegalArgumentException e) { Throwable rootCause =
	 * ExceptionUtils.getRootCause(e); throw new
	 * HttpMessageNotReadableException(e.getMessage(), rootCause,
	 * serverHttpRequest); } }
	 */
	
}
