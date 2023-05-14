package com.ServicePro.ServicePro.models;

import com.ServicePro.ServicePro.TemplateMethod.RequerimentoMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
public class RequerimentoProjetor extends RequerimentoMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @NotEmpty
    private String nomeProjetor;

    @NotEmpty
    private String modelo;



}
