package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import com.algaworks.algafood.api.assembler.CozinhaInputDisassembler;
import com.algaworks.algafood.api.assembler.CozinhaModelAssembler;
import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.api.model.input.CozinhaInput;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

//@Controller  -> está dentro da anotação @RestController
//@ResponseBody  -> está dentro da anotação @RestController
@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Autowired
	private CozinhaModelAssembler cozinhaModelAssembler;
	
	@Autowired
	private CozinhaInputDisassembler cozinhaInputDisassembler;
	
	/* versao 1
	 * @GetMapping public List<Cozinha> listar() { return
	 * cozinhaRepository.findAll(); }
	 */

	@GetMapping
	public Page<CozinhaModel> listar(@PageableDefault(size = 10) Pageable pageable) {
		Page<Cozinha> cozinhasPage = cozinhaRepository.findAll(pageable);
		
		List<CozinhaModel> cozinhasModel = cozinhaModelAssembler
				.toCollectionModel(cozinhasPage.getContent());
		
		Page<CozinhaModel> cozinhasModelPage = new PageImpl<>(cozinhasModel, pageable, 
				cozinhasPage.getTotalElements());
		
		return cozinhasModelPage;
	}
	
	/* Versao 1 com uso do optional
	 * 
	 * @GetMapping("/{cozinhaId}") public ResponseEntity<Cozinha>
	 * buscar(@PathVariable Long cozinhaId) { // return
	 * cozinhaRepository.buscar(cozinhaId);
	 * 
	 * Optional<Cozinha> cozinha = cozinhaRepository.findById(cozinhaId);
	 * 
	 * // return ResponseEntity.status(HttpStatus.OK).build(); // Devolve a
	 * requisição // com 200 mas sem body // return
	 * ResponseEntity.status(HttpStatus.OK).body(cozinha); // Devolve 200 com //
	 * body // return ResponseEntity.ok(cozinha); // idem ao de cima mas
	 * simplificado
	 * 
	 * 
	 * HttpHeaders headers = new HttpHeaders(); headers.add(HttpHeaders.LOCATION,
	 * "http://api.algafood.local:8080/cozinhas"); return
	 * ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
	 * 
	 * 
	 * if (cozinha.isPresent()) { return ResponseEntity.ok(cozinha.get()); }
	 * 
	 * // return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); return
	 * ResponseEntity.notFound().build(); }
	 */

	/* versao 2
	 * 
	 * @GetMapping("/{cozinhaId}") public Cozinha buscar(@PathVariable Long
	 * cozinhaId) { return cadastroCozinha.buscarOuFalhar(cozinhaId); }
	 */
	
	@GetMapping("/{cozinhaId}")
	public CozinhaModel buscar(@PathVariable Long cozinhaId) {
		Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);
		
		return cozinhaModelAssembler.toModel(cozinha);
	}
		
	/* versao 1
	 * @PostMapping
	 * 
	 * @ResponseStatus(HttpStatus.CREATED) public void adicionar(@RequestBody
	 * Cozinha cozinha) { cadastroCozinha.salvar(cozinha); }
	 */
	
	/* Versao 2
	 * @PostMapping
	 * 
	 * @ResponseStatus(HttpStatus.CREATED) public Cozinha
	 * adicionar(@RequestBody @Valid Cozinha cozinha) { return
	 * cadastroCozinha.salvar(cozinha); }
	 */

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {
		Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
		cozinha = cadastroCozinha.salvar(cozinha);
		
		return cozinhaModelAssembler.toModel(cozinha);
	}
	
	/* Versao 1 com uso do Optional
	 * 
	 * @PutMapping("/{cozinhaId}") public ResponseEntity<Cozinha>
	 * atualizar(@RequestBody Cozinha cozinha, @PathVariable Long cozinhaId) {
	 * 
	 * Optional<Cozinha> cozinhaAtual = cozinhaRepository.findById(cozinhaId);
	 * 
	 * if (cozinhaAtual.isPresent()) { // Origem - Destino - Propriedades para
	 * desprezar BeanUtils.copyProperties(cozinha, cozinhaAtual.get(), "id");
	 * 
	 * Cozinha cozinhaSalva = cadastroCozinha.salvar(cozinhaAtual.get()); return
	 * ResponseEntity.ok(cozinhaSalva); } return ResponseEntity.notFound().build();
	 * }
	 */

	
	/* versao 2
	 * 
	 * @PutMapping("/{cozinhaId}") public Cozinha atualizar(@PathVariable Long
	 * cozinhaId,
	 * 
	 * @RequestBody Cozinha cozinha) { Cozinha cozinhaAtual =
	 * cadastroCozinha.buscarOuFalhar(cozinhaId);
	 * 
	 * BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");
	 * 
	 * return cadastroCozinha.salvar(cozinhaAtual); }
	 */
	
	@PutMapping("/{cozinhaId}")
	public CozinhaModel atualizar(@PathVariable Long cozinhaId,
			@RequestBody @Valid CozinhaInput cozinhaInput) {
		Cozinha cozinhaAtual = cadastroCozinha.buscarOuFalhar(cozinhaId);
		cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
		cozinhaAtual = cadastroCozinha.salvar(cozinhaAtual);
		
		return cozinhaModelAssembler.toModel(cozinhaAtual);
	}
	
	/*
	 * @DeleteMapping("/{cozinhaId}") public ResponseEntity<Cozinha>
	 * remover(@PathVariable Long cozinhaId) {
	 * 
	 * try { cadastroCozinha.excluir(cozinhaId); return
	 * ResponseEntity.noContent().build();
	 * 
	 * // } catch (EntidadeNaoEncontradaException e) { // return
	 * ResponseEntity.notFound().build();
	 * 
	 * Outra maneira de fazer com uma resposta rapida
	 * 
	 * return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nao encontrado");
	 * 
	 * } catch (EntidadeEmUsoException e) { return
	 * ResponseEntity.status(HttpStatus.CONFLICT).build(); }
	 * 
	 * /* try { Cozinha cozinha = cozinhaRepository.buscar(cozinhaId);
	 * 
	 * if(cozinha != null) { cozinhaRepository.remover(cozinha); return
	 * ResponseEntity.noContent().build(); }
	 * 
	 * return ResponseEntity.notFound().build(); } catch
	 * (DataIntegrityViolationException e) { return
	 * ResponseEntity.status(HttpStatus.CONFLICT).build(); }
	 * 
	 * }
	 */

	@DeleteMapping("/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cozinhaId) {
		cadastroCozinha.excluir(cozinhaId);
	}

}