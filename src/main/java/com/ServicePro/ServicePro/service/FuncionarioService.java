package com.ServicePro.ServicePro.service;

import com.ServicePro.ServicePro.models.Funcionario;
import com.ServicePro.ServicePro.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository fr;


    private Funcionario func;
    public void salvarFuncionario(Funcionario funcionario) {

        fr.save(funcionario);
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

}
