package com.algaworks.algafood.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Grupo {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;

	@JsonIgnore
	@ManyToMany
				// Customização da tabela geradora do cruzamento de grupo x Permissao
	@JoinTable(name = "grupos_permissao", //nome da tabela gerada com o cruzamento das entidades Grupo e Permissao
			joinColumns = @JoinColumn(name = "group_id"),
			inverseJoinColumns = @JoinColumn(name = "permissao_id"))
	private List<Permissao> permissoes = new ArrayList<>();

	public boolean removerPermissao(Permissao permissao) {
		return getPermissoes().remove(permissao);
	}
	
	public boolean adicionarPermissao(Permissao permissao) {
		return getPermissoes().add(permissao);
	}
}
