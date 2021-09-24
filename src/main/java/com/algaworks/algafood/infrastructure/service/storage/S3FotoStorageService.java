package com.algaworks.algafood.infrastructure.service.storage;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.core.storage.StorageProperties;
import com.algaworks.algafood.domain.service.FotoStorageService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

//@Service
public class S3FotoStorageService implements FotoStorageService{

	//-> eu só consigo injetar uma instancia de AmazonS3 porque implementamos na classe AmazonS3Config um @Bean spring para gerar uma instancia
	//   do tipo AmazonS3
	@Autowired
	private AmazonS3 amazonS3;
	
	@Autowired
	private StorageProperties storageProperties;
	
	@Override
	public FotoRecuperada recuperar(String nomeArquivo) {
		
		String caminhoArquivo = getCaminhoArquivo(nomeArquivo);
		
		URL url = amazonS3.getUrl(storageProperties.getS3().getBucket(), caminhoArquivo);
		
		return 	FotoRecuperada.builder()
				.url(url.toString()).build();
	}

	@Override
	public void armazenar(NovaFoto novaFoto) {
		try {
			String caminhoArquivo = getCaminhoArquivo(novaFoto.getNomeArquivo());

			var objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType(novaFoto.getContentType());

			// aqui estamos preparando o payload da requisição que vamos fazer para a API da Amazon S3.
			var putObjectRequest = new PutObjectRequest(
					storageProperties.getS3().getBucket(), 
					caminhoArquivo,
					novaFoto.getInputStream(), 
					objectMetadata)
				.withCannedAcl(CannedAccessControlList.PublicRead);

			// aqui estamos fazendo a requisição para a api da amazon.
			amazonS3.putObject(putObjectRequest);
		} catch (Exception e) {
			throw new StorageException("Não foi possível enviar arquivo para Amazon S3.", e);
		}
	}

	private String getCaminhoArquivo(String nomeArquivo) {
		return String.format("%s/%s", storageProperties.getS3().getDiretorioFotos(), nomeArquivo);
	}

	@Override
	public void remover(String nomeArquivo) {
		
		try {
			String caminhoArquivo = getCaminhoArquivo(nomeArquivo);

			// aqui estamos preparando o payload da requisição que vamos fazer para a API da Amazon S3.
			var deleteObjectRequest = new DeleteObjectRequest(storageProperties.getS3().getBucket(), caminhoArquivo);
			
			// aqui estamos fazendo a requisição para a api da amazon.
			amazonS3.deleteObject(deleteObjectRequest);
		} catch (Exception e) {
			throw new StorageException("Não foi possível excluir arquivo na Amazon S3.", e);
		}
	}

}
