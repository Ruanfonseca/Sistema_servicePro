package com.ServicePro.ServicePro.utils;


import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Service
public class FormatadorUtil {

    public static LocalDateTime FormatadorDeData(LocalDateTime data) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
        String dataFormatada = data.format(formatter);

        return LocalDateTime.parse(dataFormatada, formatter);
    }

}
