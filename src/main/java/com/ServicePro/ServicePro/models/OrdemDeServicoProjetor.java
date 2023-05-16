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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requerimento_id",referencedColumnName = "codigo")
    private RequerimentoProjetor requerimentoProjetor;

    public OrdemDeServicoProjetor(LocalDateTime diaFechamento, Funcionario funcionario,
                                  RequerimentoProjetor requerimentoProjetor) {
        this.diaFechamento = diaFechamento;
        this.funcionario = funcionario;
        this.requerimentoProjetor = requerimentoProjetor;
    }

}
