package com.ServicePro.ServicePro.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Requerimento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long codigo;
	
	@NotEmpty
	private String titulo;
	
	@NotEmpty
	private String descricao;
	
	@Column(unique = true)
	private String cpf;

	@Column(unique = true)
	private String matricula;

	@NotEmpty
	private String nomeRequerente;
	
	@NotEmpty
	private String email;

	private String mensagemRetorno;
	
	private LocalDateTime data = LocalDateTime.now();
	
	@NotEmpty
	private String senha;
	
	@NotEmpty
	private String status = "Pendente";
	
	@ManyToOne
	private Funcionario funcionario;

	@OneToOne(mappedBy = "requerimento")
	private OrdemDeServico ordemDeServico;

}