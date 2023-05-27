package com.ServicePro.ServicePro.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrdemDeServicoProjetor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime diaFechamento;


    private String nomeFuncionarioResponsavel;
    private String matriculaFuncionario;

    private String requerenteNome;
    private String requerenteMatricula;


    public OrdemDeServicoProjetor(LocalDateTime data, String matriculaFuncionario, String nomeFuncionarioResponsavel,
                              String matriculaReq, String nomeRequerente) {
        this.diaFechamento = data;
        this.nomeFuncionarioResponsavel = nomeFuncionarioResponsavel;
        this.matriculaFuncionario = matriculaFuncionario;
        this.requerenteNome = nomeRequerente;
        this.requerenteMatricula = matriculaReq;
    }
}
