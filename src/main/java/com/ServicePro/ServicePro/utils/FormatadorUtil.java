package com.ServicePro.ServicePro.utils;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatadorUtil {

    public static LocalDateTime FormatadorDeData(LocalDateTime data) {
        // Obtém a data e hora atual
        LocalDateTime agora = LocalDateTime.now();

        // Define o padrão desejado para obter apenas o dia
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");

        // Formata a data para obter apenas o dia
        String diaFormatado = agora.format(formatter);

        // Cria um novo LocalDateTime com apenas o dia, mantendo o restante dos valores padrão
        data = LocalDateTime.of(agora.getYear(), agora.getMonth(), Integer.parseInt(diaFormatado), 0, 0, 0);

     return data;
    }

}
