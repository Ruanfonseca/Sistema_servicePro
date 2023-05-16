package com.ServicePro.ServicePro.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
public class RequerimentoSala{

    @NotEmpty
    private String numeroSala;

    @NotEmpty
    private String dataDeUso;

    @NotEmpty
    private String horaInicial;

    @NotEmpty
    private String horaFinal;

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
    @NotEmpty
    private String status = "Pendente";

    @ManyToOne
    private Funcionario funcionario;

    @OneToOne(mappedBy = "requerimento")
    private OrdemDeServico ordemDeServico;
}
