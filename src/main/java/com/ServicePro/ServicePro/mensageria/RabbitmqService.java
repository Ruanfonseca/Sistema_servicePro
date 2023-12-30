package com.ServicePro.ServicePro.mensageria;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitmqService {

    @Autowired
 private RabbitTemplate rabbitTemplate;


    @Autowired
    private ObjectMapper objectMapper;


    public void enviaMensagem(String nomefila,Object mensagem){
        try {
            //converte a mensagem para um json e envia
            String mensagemjson = this.objectMapper.writeValueAsString(mensagem);
            this.rabbitTemplate.convertAndSend(nomefila,mensagemjson);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
