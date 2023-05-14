package com.ServicePro.ServicePro.models;

import com.ServicePro.ServicePro.TemplateMethod.RequerimentoMethod;
import lombok.*;

import javax.persistence.Entity;

@Entity
@Data
public class Requerimento extends RequerimentoMethod {
    // A classe Requerimento herda todas as propriedades e mapeamentos da classe RequerimentoMethod
    // Não é necessário definir as propriedades novamente
    // Usa o tipo Wrapper Long para chaves primárias
}