package com.ServicePro.ServicePro.models;

import com.ServicePro.ServicePro.abstractFactory.DadosRabbitMq;

public class DadosRabbitmq extends DadosRabbitMq {

    public DadosRabbitmq(String titulo, String mensagem) {
        super(titulo, mensagem);
    }
}
