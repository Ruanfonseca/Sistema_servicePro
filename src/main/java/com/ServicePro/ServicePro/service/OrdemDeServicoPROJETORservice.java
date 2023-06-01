package com.ServicePro.ServicePro.service;

import com.ServicePro.ServicePro.models.OrdemDeServicoProjetor;
import com.ServicePro.ServicePro.repository.OrdemDeServicoProjetorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdemDeServicoPROJETORservice {
    @Autowired
    private OrdemDeServicoProjetorRepository repository;


    public void salvar(OrdemDeServicoProjetor ordemDeServicoProjetor) {
    repository.save(ordemDeServicoProjetor);
    }
}
