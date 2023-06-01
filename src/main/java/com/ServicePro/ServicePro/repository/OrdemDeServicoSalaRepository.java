package com.ServicePro.ServicePro.repository;

import com.ServicePro.ServicePro.models.OrdemDeServicoSala;
import org.springframework.data.repository.CrudRepository;

public interface OrdemDeServicoSalaRepository extends CrudRepository<OrdemDeServicoSala, Long> {


    OrdemDeServicoSala findById(long id);


}
