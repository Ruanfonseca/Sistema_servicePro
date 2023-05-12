package com.ServicePro.ServicePro.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Locale;
@Entity
@Getter
@Setter
@NoArgsConstructor
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

    public OrdemDeServico(LocalDateTime diaFechamento, Funcionario funcionario, Requerimento requerimento) {
        this.diaFechamento = diaFechamento;
        this.funcionario = funcionario;
        this.requerimento = requerimento;
    }

}
