package com.ServicePro.ServicePro.log;

import org.springframework.stereotype.Service;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;

import static org.codehaus.plexus.util.FileUtils.fileExists;


//classe para gravar registros em log
@Service
public class Log {
    public static void registrarLOG(String tipoREQ,String data ,String registro){
    String arq = "ArquivoDeLog.txt";
         try {
            if(fileExists(arq)){

                    FileOutputStream arquivoDeLog = new FileOutputStream("ArquivoDeLog.txt");
                    DataOutputStream gravarNoArquivo = new DataOutputStream(arquivoDeLog);
                    String aux = "Dia:  "+ data + " Operação: "+registro +" Tipo de requerimento:  "+tipoREQ+"";
                    gravarNoArquivo.writeChars("Registro de log do cliente HTTP");
                    gravarNoArquivo.writeChars(aux);

            }else{

                    File file = new File("ArquivoDeLog.txt");
                    FileOutputStream arquivoDeLog = new FileOutputStream(file);
                    DataOutputStream gravarNoArquivo = new DataOutputStream(arquivoDeLog);
                    String aux = "Dia:  "+ data + " Operação: "+registro +" Tipo de requerimento:  "+tipoREQ+"";
                    gravarNoArquivo.writeChars("Registro de log do cliente HTTP");
                    gravarNoArquivo.writeChars(aux);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
