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
public class OrdemDeServicoSala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime diaFechamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requerimento_id",referencedColumnName = "codigo")
    private RequerimentoSala requerimento;

    public OrdemDeServicoSala(LocalDateTime diaFechamento, Funcionario funcionario, RequerimentoSala requerimento) {
        this.diaFechamento = diaFechamento;
        this.funcionario = funcionario;
        this.requerimento = requerimento;
    }

}
