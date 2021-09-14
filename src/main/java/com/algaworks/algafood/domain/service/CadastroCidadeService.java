package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.CidadeRepository;

@Service
public class CadastroCidadeService {
	
	private static final String MSG_CIDADE_EM_USO 
	= "Cidade de código %d não pode ser removida, pois está em uso";

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CadastroEstadoService cadastroEstado;

	/* Versão 1 com optional
	 * 
	 * public Cidade salvar(Cidade cidade) { Long estadoId =
	 * cidade.getEstado().getId(); Optional<Estado> estado =
	 * estadoRepository.findById(estadoId);
	 * 
	 * if (estado.isEmpty()) { throw new EntidadeNaoEncontradaException(
	 * String.format("Não existe cadastro de estado com código %d", estadoId)); }
	 * 
	 * cidade.setEstado(estado.get());
	 * 
	 * return cidadeRepository.save(cidade); }
	 */
	
	@Transactional
	public Cidade salvar(Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();

		Estado estado = cadastroEstado.buscarOuFalhar(estadoId);
		
		cidade.setEstado(estado);
		
		return cidadeRepository.save(cidade);
	}
	
	
	/* versao 1 
	 * 
	 * public void excluir(Long cidadeId) { try {
	 * cidadeRepository.deleteById(cidadeId);
	 * 
	 * } catch (EmptyResultDataAccessException e) { throw new
	 * EntidadeNaoEncontradaException(
	 * String.format("Não existe um cadastro de cidade com código %d", cidadeId));
	 * 
	 * } catch (DataIntegrityViolationException e) { throw new
	 * EntidadeEmUsoException(
	 * String.format("Cidade de código %d não pode ser removida, pois está em uso",
	 * cidadeId)); } }
	 */
	
	@Transactional
	public void excluir(Long cidadeId) {
		try {
			cidadeRepository.deleteById(cidadeId);
			cidadeRepository.flush();			
		} catch (EmptyResultDataAccessException e) {
			throw new CidadeNaoEncontradaException(cidadeId);
		
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
				String.format(MSG_CIDADE_EM_USO, cidadeId));
		}
	}
	
	public Cidade buscarOuFalhar(Long cidadeId) {
		return cidadeRepository.findById(cidadeId)
			.orElseThrow(() -> new CidadeNaoEncontradaException(cidadeId));
	}
}
