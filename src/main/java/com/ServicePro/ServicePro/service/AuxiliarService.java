package com.ServicePro.ServicePro.service;

import com.ServicePro.ServicePro.models.Auxiliar;
import com.ServicePro.ServicePro.models.Funcionario;
import com.ServicePro.ServicePro.repository.AuxiliarRepository;
import com.ServicePro.ServicePro.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void salvar(Auxiliar auxiliar) {
      Aux.save(auxiliar);
    }

    public void deletar(Auxiliar auxiliar) {
    deletar(auxiliar);
    }
}
