package com.ServicePro.ServicePro.TemplateMethod;

import com.ServicePro.ServicePro.models.Funcionario;
import com.ServicePro.ServicePro.models.OrdemDeServico;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequerimentoMethod implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private String senha;

    @NotEmpty
    private String status = "Pendente";

    @ManyToOne
    private Funcionario funcionario;

    @OneToOne(mappedBy = "requerimento")
    private OrdemDeServico ordemDeServico;
}


