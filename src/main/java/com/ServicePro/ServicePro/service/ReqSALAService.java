package com.ServicePro.ServicePro.service;

import com.ServicePro.ServicePro.models.RequerimentoSala;
import com.ServicePro.ServicePro.repository.RequerimentoSalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReqSALAService {

    @Autowired
    private RequerimentoSalaRepository repository;


    public RequerimentoSala BuscarPorCpf(String cpf) {
      return repository.findByCpf(cpf);
    }

    public boolean salvar(RequerimentoSala req) {
        try {
           repository.save(req);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
     }

    public Page<RequerimentoSala> BuscarStatusFinalizado(Pageable pageable) {
        return repository.findByStatusFinalizado(pageable);
    }


    public Page<RequerimentoSala> BuscarStatusPendente(Pageable pageable) {
        return repository.findByStatusPendente(pageable);
    }

    public RequerimentoSala buscarPorCodigo(long codigo) {
        return repository.findByCodigo(codigo);
    }

    public void deletar(RequerimentoSala req) {
        repository.delete(req);
    }
}
