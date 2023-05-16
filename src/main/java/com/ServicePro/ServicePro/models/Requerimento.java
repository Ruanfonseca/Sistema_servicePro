package com.ServicePro.ServicePro.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Data
public class Requerimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;


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

    private String senha;

    @NotEmpty
    private String status = "Pendente";

    @ManyToOne
    private Funcionario funcionario;

    @OneToOne(mappedBy = "requerimento")
    private OrdemDeServico ordemDeServico;


}