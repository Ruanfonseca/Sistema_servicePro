package com.ServicePro.ServicePro.service;

import com.ServicePro.ServicePro.models.RequerimentoProjetor;
import com.ServicePro.ServicePro.repository.RequerimentoProjetorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReqPROJETORService {

    @Autowired
    private RequerimentoProjetorRepository repository;

    public RequerimentoProjetor buscarPorCpf(String cpf) {
        return  repository.findByCpf(cpf);
    }

    public boolean salvar(RequerimentoProjetor req) {
        try {
            repository.save(req);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public Page<RequerimentoProjetor> buscarPorStatusFinalizado(Pageable pageable) {
        return repository.findByStatusFinalizado(pageable);
    }

    public Page<RequerimentoProjetor> buscarPorStatusPendente(Pageable pageable) {
        return repository.findByStatusPendente(pageable);
    }

    public RequerimentoProjetor buscrPorCodigo(long codigo) {
        return repository.findByCodigo(codigo);
    }

    public void deletar(RequerimentoProjetor req) {
        repository.delete(req);
    }
}
