package com.ServicePro.ServicePro.component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static com.ServicePro.ServicePro.log.Log.registrarLOG;

/*Classe para criar uma conexão e envio da requisição para o rabbitMq */
@Component
public class clienteHTTP {

    private final RestTemplate restTemplate;

    @Autowired
    public clienteHTTP(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void criaHrequestHTTP(Integer flag,String msg) {

        switch (flag) {
            case 1:
                String url = "https://api.rabbitMQ.com/enviar-dados/wifi";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                // Criando o objeto HttpEntity com os dados e o cabeçalho
                HttpEntity<String> requestEntity = new HttpEntity<>(msg, headers);

                // Enviando a requisição POST
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);

                // Verificar se a requisição foi bem-sucedida ou não
                if (responseEntity.getStatusCode().is1xxInformational()) {
                    String response = responseEntity.getBody();
                    registrarNoTXT(flag , response);
                }
                break;

            case 2:
                String url2 = "https://api.rabbitMQ.com/enviar-dados/sala";

                HttpHeaders headers2 = new HttpHeaders();
                headers2.setContentType(MediaType.APPLICATION_JSON);

                // Criando o objeto HttpEntity com os dados e o cabeçalho
                HttpEntity<String> requestEntity2 = new HttpEntity<>(msg, headers2);

                // Enviando a requisição POST
                ResponseEntity<String> responseEntity2 = restTemplate.postForEntity(url2, requestEntity2, String.class);

                // Verificar se a requisição foi bem-sucedida ou não
                if (responseEntity2.getStatusCode().is1xxInformational()) {
                    String response = responseEntity2.getBody();
                    registrarNoTXT(flag , response);
                }
                break;

            case 3:
                String url3 = "https://api.rabbitMQ.com/enviar-dados/projetor";

                HttpHeaders headers3 = new HttpHeaders();
                headers3.setContentType(MediaType.APPLICATION_JSON);

                // Criando o objeto HttpEntity com os dados e o cabeçalho
                HttpEntity<String> requestEntity3 = new HttpEntity<>(msg, headers3);

                // Enviando a requisição POST
                ResponseEntity<String> responseEntity3 = restTemplate.postForEntity(url3, requestEntity3, String.class);

                // Verificar se a requisição foi bem-sucedida ou não
                if (responseEntity3.getStatusCode().is1xxInformational()) {
                    String response = responseEntity3.getBody();
                    registrarNoTXT(flag , response);
                }
                break;
        }
    }

    public static void registrarNoTXT(Integer flag , String response){

        LocalDateTime data = LocalDateTime.now();
        String FLAG = String.valueOf(flag);
        String DATA = String.valueOf(data);

        //registra no arquivo.txt
        registrarLOG(FLAG,response,DATA);
    }

}
