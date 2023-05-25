package com.ServicePro.ServicePro.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequerimentoProjetor  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;


    private String dataDeUso;


    private String horaInicial;


    private String horaFinal;

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
    private String status = "PENDENTE";

    @ManyToOne
    private Funcionario funcionario;

    @OneToOne(mappedBy = "requerimento")
    private OrdemDeServico ordemDeServico;
}
