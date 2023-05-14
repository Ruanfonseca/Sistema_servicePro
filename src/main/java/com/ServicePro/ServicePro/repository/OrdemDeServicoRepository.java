package com.ServicePro.ServicePro.repository;

import com.ServicePro.ServicePro.models.OrdemDeServico;
import org.springframework.data.repository.CrudRepository;

public interface  OrdemDeServicoRepository extends CrudRepository<OrdemDeServico, Long> {


    OrdemDeServico findById(long id);


}
