package com.ServicePro.ServicePro.template;


import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class TemplateMSG implements Serializable {


   public static long serialVersionUID = 1L;
   private String MSG;
   private String EMAIL;
   private String NOME;


   @Override
   public String toString() {
      return "TemplateMSG{" +
              "MSG='" + MSG + '\'' +
              ", EMAIL='" + EMAIL + '\'' +
              ", NOME='" + NOME + '\'' +
              '}';
   }
}
