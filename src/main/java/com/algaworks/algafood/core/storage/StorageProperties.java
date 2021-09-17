package com.algaworks.algafood.core.storage;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.amazonaws.regions.Regions;

import lombok.Getter;
import lombok.Setter;


//****
//  ==> Essa classe e suas propriedade tem ligação com as propriedades abaixo que estão inseridas no application.properties
//
//	algafood.storage.local.diretorio-fotos=/c/Users/Bruno/OneDrive/Documentos/Estudos/Spring-AlgaWorks/catalogo
//	
// 	algafood.storage.s3.id-chave-acesso=AKIAZFWFV2OLHRK2YJQM
//	algafood.storage.s3.chave-acesso-secreto=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
//	algafood.storage.s3.bucket=algafood-test-bc
//	algafood.storage.s3.regiao=us-east-1
//	algafood.storage.s3.diretorio-fotos=catalogo/
//****

@Getter
@Setter
@Component
@ConfigurationProperties("algafood.storage")
public class StorageProperties {
	
	private Local local = new Local();
	private S3 s3 = new S3();
	
	@Getter
	@Setter
	public class Local {
		private Path diretorioFotos;
	}
	
	@Getter
	@Setter
	public class S3 {
		
		private String idChaveAcesso;
		private String chaveAcessoSecreta;
		private String bucket;
		private Regions regiao;
		private String diretorioFotos;
	}

}
