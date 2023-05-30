package com.ServicePro.ServicePro.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public  class OrdemDeServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime diaFechamento;
    private String nomeFuncionarioResponsavel;
    private String matriculaFuncionario;

    private String requerenteNome;
    private String requerenteMatricula;

    public OrdemDeServico(LocalDateTime data, String matriculaFuncionario, String nomeFuncionarioResponsavel,
                          String matriculaReq, String nomeRequerente) {
    this.diaFechamento = data;
    this.nomeFuncionarioResponsavel = nomeFuncionarioResponsavel;
    this.matriculaFuncionario = matriculaFuncionario;
    this.requerenteNome = nomeRequerente;
    this.requerenteMatricula = matriculaReq;
    }
}
