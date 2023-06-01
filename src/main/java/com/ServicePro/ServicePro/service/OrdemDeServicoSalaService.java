package com.ServicePro.ServicePro.service;


import com.ServicePro.ServicePro.models.OrdemDeServicoSala;
import com.ServicePro.ServicePro.repository.OrdemDeServicoSalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdemDeServicoSalaService {
    @Autowired
    private OrdemDeServicoSalaRepository repository;

    public void salvar(OrdemDeServicoSala ordemDeServicoSala) {
     repository.save(ordemDeServicoSala);
    }
}
