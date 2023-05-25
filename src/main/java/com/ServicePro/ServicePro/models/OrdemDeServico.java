package com.ServicePro.ServicePro.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public  class OrdemDeServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime diaFechamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requerimento_id",referencedColumnName = "codigo")
    private Requerimento requerimento;

    public OrdemDeServico(LocalDateTime data, Funcionario aux, Requerimento requerimento) {
        this.diaFechamento = data;
        this.funcionario = aux;
        this.requerimento = requerimento;
    }
}
