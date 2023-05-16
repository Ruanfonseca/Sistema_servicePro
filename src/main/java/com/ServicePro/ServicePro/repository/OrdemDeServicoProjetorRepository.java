package com.ServicePro.ServicePro.repository;

import com.ServicePro.ServicePro.models.OrdemDeServicoProjetor;
import com.ServicePro.ServicePro.models.OrdemDeServicoSala;
import org.springframework.data.repository.CrudRepository;

public interface OrdemDeServicoProjetorRepository extends CrudRepository<OrdemDeServicoProjetor, Long> {


    OrdemDeServicoProjetor findById(long id);


}
