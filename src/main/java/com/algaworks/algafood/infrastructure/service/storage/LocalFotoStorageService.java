package com.algaworks.algafood.infrastructure.service.storage;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.algaworks.algafood.core.storage.StorageProperties;
import com.algaworks.algafood.domain.service.FotoStorageService;

// Pra testar local tem que descomentar a anotação @Service aqui e comentar a da classe S3FotoStorageService
//@Service
public class LocalFotoStorageService implements FotoStorageService {

	@Autowired
	private StorageProperties storageProperties;
	
//	@Value("${algafood.storage.local.diretorio-fotos}")
	private Path diretorioFotos;
	
	@Override
	public FotoRecuperada recuperar(String nomeArquivo) {
		try {
			Path arquivoPath = getArquivoPath(nomeArquivo);

			FotoRecuperada fotoRecuperada = FotoRecuperada.builder()
					.inputStream(Files.newInputStream(arquivoPath))
					.build();
			
			return fotoRecuperada;
		} catch (Exception e) {
			throw new StorageException("Não foi possível recuperar arquivo.", e);
		}
	}
	
	@Override
	public void armazenar(NovaFoto novaFoto) {
		try {
			Path arquivoPath = getArquivoPath(novaFoto.getNomeArquivo());
			
			FileCopyUtils.copy(novaFoto.getInputStream(), 
					Files.newOutputStream(arquivoPath));
		} catch (Exception e) {
			throw new StorageException("Não foi possível armazenar arquivo.", e);
		}
	}
	
	@Override
	public void remover(String nomeArquivo) {
		try {
			Path arquivoPath = getArquivoPath(nomeArquivo);
			
			Files.deleteIfExists(arquivoPath);
		} catch (Exception e) {
			throw new StorageException("Não foi possível excluir arquivo.", e);
		}
	}

	private Path getArquivoPath(String nomeArquivo) {
	//	return diretorioFotos.resolve(Path.of(nomeArquivo));
		return storageProperties.getLocal().getDiretorioFotos()
				.resolve(Path.of(nomeArquivo));
	}
}
