package com.ServicePro.ServicePro.service;

import com.ServicePro.ServicePro.models.Auxiliar;
import com.ServicePro.ServicePro.models.Funcionario;
import com.ServicePro.ServicePro.repository.AuxiliarRepository;
import com.ServicePro.ServicePro.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<Auxiliar> aux = Optional.ofNullable(buscarPorCPF(auxiliar));
        if(aux.isPresent()) {
            return false;
        }else{
            Aux.save(auxiliar);
            return true;
        }

    }

    public void deletar(Auxiliar auxiliar) {
    deletar(auxiliar);
    }

    public List<Auxiliar> buscarPorNomesAuxiliares(String nome) {
        return Aux.findByNomesAuxiliares(nome);
    }

}

