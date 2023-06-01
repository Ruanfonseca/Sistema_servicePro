package com.ServicePro.ServicePro.service;

import com.ServicePro.ServicePro.models.OrdemDeServico;
import com.ServicePro.ServicePro.repository.OrdemDeServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdemDeServicoService {
    @Autowired
    private OrdemDeServicoRepository OS;

    public void salva(OrdemDeServico ordemDeServico) {
        OS.save(ordemDeServico);
    }
}
