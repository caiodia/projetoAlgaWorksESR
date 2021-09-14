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

import com.algaworks.algafood.api.assembler.CidadeInputDisassembler;
import com.algaworks.algafood.api.assembler.CidadeModelAssembler;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.api.model.input.CidadeInput;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

@RestController
@RequestMapping(value = "/cidades")
public class CidadeController {

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CadastroCidadeService cadastroCidade;
	
	@Autowired
	private CidadeModelAssembler cidadeModelAssembler;
	
	@Autowired
	private CidadeInputDisassembler cidadeInputDisassembler;
		
	/* Versao 1
	 * @GetMapping public List<Cidade> listar() { return cidadeRepository.findAll();
	 * }
	 */
	
	@GetMapping
	public List<CidadeModel> listar() {
		List<Cidade> todasCidades = cidadeRepository.findAll();
		
		return cidadeModelAssembler.toCollectionModel(todasCidades);
	}
		
	/* Versao 1 com uso do optional
	 * @GetMapping("/{cidadeId}") public ResponseEntity<Cidade> buscar(@PathVariable
	 * Long cidadeId) { Optional<Cidade> cidade =
	 * cidadeRepository.findById(cidadeId);
	 * 
	 * if (cidade.isPresent()) { return ResponseEntity.ok(cidade.get()); }
	 * 
	 * return ResponseEntity.notFound().build(); }
	 */

	/* Versao 2 
	 * @GetMapping("/{cidadeId}") public Cidade buscar(@PathVariable Long cidadeId)
	 * { return cadastroCidade.buscarOuFalhar(cidadeId); }
	 */
	
	@GetMapping("/{cidadeId}")
	public CidadeModel buscar(@PathVariable Long cidadeId) {
		Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId);
		
		return cidadeModelAssembler.toModel(cidade);
	}
	
	/* versao 1 com uso do optional
	 * 
	 * @PostMapping public ResponseEntity<?> adicionar(@RequestBody Cidade cidade) {
	 * try { cidade = cadastroCidade.salvar(cidade);
	 * 
	 * return ResponseEntity.status(HttpStatus.CREATED) .body(cidade); } catch
	 * (EntidadeNaoEncontradaException e) { return ResponseEntity.badRequest()
	 * .body(e.getMessage()); } }
	 */
	
	/* versao 2
	 * @PostMapping
	 * 
	 * @ResponseStatus(HttpStatus.CREATED) public Cidade adicionar(@RequestBody
	 * Cidade cidade) { try { return cadastroCidade.salvar(cidade); } catch
	 * (EstadoNaoEncontradoException e) { throw new NegocioException(e.getMessage(),
	 * e); } }
	 */
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeModel adicionar(@RequestBody @Valid CidadeInput cidadeInput) {
		try {
			Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);
			
			cidade = cadastroCidade.salvar(cidade);
			
			return cidadeModelAssembler.toModel(cidade);
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	/* Versao 1 com uso do optional
	 * 
	 * @PutMapping("/{cidadeId}") public ResponseEntity<?> atualizar(@PathVariable
	 * Long cidadeId,
	 * 
	 * @RequestBody Cidade cidade) { try { Optional<Cidade> cidadeAtual =
	 * cidadeRepository.findById(cidadeId);
	 * 
	 * if (cidadeAtual != null) { BeanUtils.copyProperties(cidade, cidadeAtual,
	 * "id");
	 * 
	 * Cidade cidadeSalva = cadastroCidade.salvar(cidadeAtual.get()); return
	 * ResponseEntity.ok(cidadeSalva); }
	 * 
	 * return ResponseEntity.notFound().build();
	 * 
	 * } catch (EntidadeNaoEncontradaException e) { return
	 * ResponseEntity.badRequest() .body(e.getMessage()); } }
	 */
	
	/* versao 2 
	 * @PutMapping("/{cidadeId}") public Cidade atualizar(@PathVariable Long
	 * cidadeId,
	 * 
	 * @RequestBody Cidade cidade) { try { Cidade cidadeAtual =
	 * cadastroCidade.buscarOuFalhar(cidadeId);
	 * 
	 * BeanUtils.copyProperties(cidade, cidadeAtual, "id");
	 * 
	 * return cadastroCidade.salvar(cidadeAtual); } catch
	 * (EstadoNaoEncontradoException e) { throw new NegocioException(e.getMessage(),
	 * e); } }
	 */
	
	@PutMapping("/{cidadeId}")
	public CidadeModel atualizar(@PathVariable Long cidadeId,
			@RequestBody @Valid CidadeInput cidadeInput) {
		try {
			Cidade cidadeAtual = cadastroCidade.buscarOuFalhar(cidadeId);
			
			cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);
			
			cidadeAtual = cadastroCidade.salvar(cidadeAtual);
			
			return cidadeModelAssembler.toModel(cidadeAtual);
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	/* versao 1
	 * 
	 * @DeleteMapping("/{cidadeId}") public ResponseEntity<Cidade>
	 * remover(@PathVariable Long cidadeId) { try {
	 * cadastroCidade.excluir(cidadeId); return ResponseEntity.noContent().build();
	 * 
	 * } catch (EntidadeNaoEncontradaException e) { return
	 * ResponseEntity.notFound().build();
	 * 
	 * } catch (EntidadeEmUsoException e) { return
	 * ResponseEntity.status(HttpStatus.CONFLICT).build(); } }
	 */
	
	@DeleteMapping("/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cidadeId) {
		cadastroCidade.excluir(cidadeId);	
	}
}
