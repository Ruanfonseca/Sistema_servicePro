package com.ServicePro.ServicePro.service;

import com.ServicePro.ServicePro.models.Auxiliar;
import com.ServicePro.ServicePro.models.Funcionario;
import com.ServicePro.ServicePro.repository.AuxiliarRepository;
import com.ServicePro.ServicePro.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuxiliarService {

    @Autowired
    private FuncionarioRepository fr;

    @Autowired
    private AuxiliarRepository Aux;


    public Iterable<Auxiliar> buscarPor(Funcionario funcionario) {
        return Aux.findByFuncionario(funcionario);
    }

    public Auxiliar buscarPorCPF(Auxiliar auxiliar) {
        return Aux.findByCpf(auxiliar.getCpf());
    }

    public Auxiliar buscarPorCPF(String cpf) {
        return Aux.findByCpf(cpf);
    }

    public boolean salvar(Auxiliar auxiliar) {
        try {
            Auxiliar aux = buscarPorCPF(auxiliar.getCpf());
            //se for null é porque não existe nenhum usuario no banco com o mesmo cpf
            if(aux != null){

                    if (aux.getCpf() == auxiliar.getCpf()) {
                        return false;
                    } else {
                        Aux.save(auxiliar);
                        return true;
                    }
            }else{
                Aux.save(auxiliar);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }




    }

    public void deletar(Auxiliar auxiliar) {
        deletar(auxiliar);
    }

    public List<Auxiliar> buscarPorNomesAuxiliares(String nome) {
        return Aux.findByNomesAuxiliares(nome);
    }

}

