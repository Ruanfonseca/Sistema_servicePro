package com.ServicePro.ServicePro.utils;

import org.springframework.stereotype.Service;

@Service
public class ValidacaoUtil {

    public static boolean validarCPF(String cpf) {
        cpf = cpf.replaceAll("\\D", ""); // remove caracteres não numéricos

        // CPF deve ter 11 caracteres numéricos
        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Calcula o primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        int resto = 11 - (soma % 11);
        int digito1 = (resto == 10 || resto == 11) ? 0 : resto;

        // Calcula o segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        resto = 11 - (soma % 11);
        int digito2 = (resto == 10 || resto == 11) ? 0 : resto;

        // Verifica se os dígitos verificadores estão corretos
        return (cpf.charAt(9) - '0' == digito1 && cpf.charAt(10) - '0' == digito2);
    }
}
