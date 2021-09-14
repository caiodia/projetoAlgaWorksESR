package com.algaworks.algafood;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroCozinhaIT{

// Testes de API - Onde fazemos uma chamada Http
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	private static final int COZINHA_ID_INEXISTENTE = 100;

	private Cozinha cozinhaAmericana;
	private int quantidadeCozinhasCadastradas;
	private String jsonCorretoCozinhaChinesa;
	
	@Before
	public void setUp() {
		// este método loga a request e response se falhar o teste.
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

		RestAssured.port = port;
		RestAssured.basePath = "/cozinhas";
		
		jsonCorretoCozinhaChinesa = ResourceUtils.getContentFromResource(
				"/json/correto/cozinha-chinesa.json");
		
		databaseCleaner.clearTables();
		prepararDados();
	}

	@Test
	public void deveRetornarStatus200QuandoConsultarCozinhas() {
		
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}


	//objetivo : validar o corpo da resposta
	@Test
	public void deveConterNCozinhasQuandoConsultarCozinhas() {
	
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", Matchers.hasSize(quantidadeCozinhasCadastradas))
			.body("nome", Matchers.hasItems("Indiana", "Tailandesa"));
	}
	
	// objetivo : incluir uma nova cozinha
	@Test
	public void testRetornarStatus201_QuandoCadastrarCozinha() {
		given()
			.body(jsonCorretoCozinhaChinesa)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
	@Test
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarCozinhaExistente() {
		given()
			.pathParam("cozinhaId", cozinhaAmericana.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{cozinhaId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", equalTo(cozinhaAmericana.getNome()));
	}
	
	@Test
	public void deveRetornarStatus404_QuandoConsultarCozinhaInexistente() {
		given()
			.pathParam("cozinhaId", COZINHA_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/{cozinhaId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	private void prepararDados() {
		Cozinha cozinha1 = new Cozinha();
		cozinha1.setNome("Tailandesa");
		cozinhaRepository.save(cozinha1);

		Cozinha cozinha2 = new Cozinha();
		cozinha2.setNome("Indiana");
		cozinhaRepository.save(cozinha2);
		
		cozinhaAmericana  = new Cozinha();
		cozinhaAmericana.setNome("Americana");
		cozinhaRepository.save(cozinhaAmericana);
		
		quantidadeCozinhasCadastradas = (int) cozinhaRepository.count();
	}
	
	/* Testes de integração
	 * 
	 * 
	 * 	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	 * 
	 * @Test public void testarCadastroCozinhaComSucesso(){ // cenario
	 * 
	 * Cozinha novaCozinha = new Cozinha(); novaCozinha.setNome("Chinesa");
	 * 
	 * // ação
	 * 
	 * novaCozinha = cadastroCozinha.salvar(novaCozinha);
	 * 
	 * // validação
	 * 
	 * assertThat(novaCozinha).isNotNull();
	 * assertThat(novaCozinha.getId()).isNotNull();
	 * 
	 * }
	 * 
	 * @Test(expected = ConstraintViolationException.class) public void
	 * deveFalharAoCadastrarCozinhaQuandoSemNome() { // cenario
	 * 
	 * Cozinha novaCozinha = new Cozinha(); novaCozinha.setNome(null);
	 * 
	 * // ação
	 * 
	 * novaCozinha = cadastroCozinha.salvar(novaCozinha); }
	 * 
	 * @Test(expected = EntidadeEmUsoException.class) public void
	 * deveFalharQuandoExcluirCozinhaEmUso() {
	 * 
	 * Cozinha cozinha = new Cozinha(); cozinha.setId(1L);
	 * 
	 * cadastroCozinha.excluir(cozinha.getId()); }
	 * 
	 * @Test(expected = CozinhaNaoEncontradaException.class) public void
	 * deveFalharQuandoExcluirCozinhaInexistente() {
	 * 
	 * Cozinha cozinha = new Cozinha(); cozinha.setId(20L);
	 * 
	 * cadastroCozinha.excluir(cozinha.getId()); }
	 */
}
