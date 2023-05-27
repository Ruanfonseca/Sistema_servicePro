package com.ServicePro.ServicePro.models;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Funcionario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;


	private String Tipo;

	private String nome;

	@Column(unique = true)
	private String cpf;
	private String data;
	private String email;

	@Column(unique = true)
	private String matricula;

	@OneToMany(mappedBy = "funcionario", cascade = CascadeType.REMOVE)
	private List<Auxiliar>auxiliares;


}