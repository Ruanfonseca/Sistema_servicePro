package com.ServicePro.ServicePro.service;

import com.ServicePro.ServicePro.models.Requerimento;
import com.ServicePro.ServicePro.repository.RequerimentoWIfiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReqWifiService {

    @Autowired
    private RequerimentoWIfiRepository repository;

    private Requerimento requerimento;

    public Requerimento buscarPorCPF(String cpf) {
        return repository.findByCpf(cpf);
    }

    public boolean salvar(Requerimento req) {
       try {
           repository.save(req);
           return true;
       }catch (Exception e){
           e.printStackTrace();
       }
       return false;
    }
    public void salva(Requerimento requerimento) {
            repository.save(requerimento);
    }


    public Page<Requerimento> buscarPorStatusFinalizado(Pageable pageable) {
        return  repository.findByStatusFinalizado(pageable);
    }

    public List<Requerimento> buscarPorStatusFinalizado() {
        return  repository.findByStatusFinalizado();
    }
    public Page<Requerimento> buscarPorStatusPendente(Pageable pageable) {
        return  repository.findByStatusPendente(pageable);
    }

    public Requerimento buscarPorCodigo(long codigo) {
        return repository.findByCodigo(codigo);

    }

    public void deletar(Requerimento req) {
        repository.delete(req);
    }
}
