package com.ServicePro.ServicePro.service;

import com.ServicePro.ServicePro.models.Funcionario;
import com.ServicePro.ServicePro.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository fr;

    private Funcionario func;


    public boolean salvarFuncionario(Funcionario funcionario) {

        Optional<Funcionario> aux = Optional.ofNullable(buscarPorCPF(funcionario.getCpf()));
        if(aux.isPresent()) {
            return false;
        }else{
            fr.save(funcionario);
            return true;
        }

    }

    public List<Funcionario> buscarPorNome(String nome){
        return fr.findByNomes(nome);
    }

    public Page<Funcionario> encontrarTodosPaginacao(Pageable pageable) {
        return fr.findall(pageable);
    }

    public Funcionario encontrarPorId(long id) {
        return fr.findById(id);
    }


    public void deletar(Funcionario funcionario) {
       fr.delete(funcionario);
    }

    public Funcionario buscarPorCPF(String cpf) {
      return fr.findByCPF(cpf);
    }

    public Funcionario buscarPorMatricula(String matricula) {
        return fr.findByMatricula(matricula);
    }

    public Iterable<Funcionario> BuscarPorsetor(String setor) {
        return fr.findAllsetor(setor);
    }
}
