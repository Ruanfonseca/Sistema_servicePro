package com.ServicePro.ServicePro.repository;

import com.ServicePro.ServicePro.models.Funcionario;
import com.ServicePro.ServicePro.models.OrdemDeServico;
import com.ServicePro.ServicePro.models.Requerimento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface  OrdemDeServicoRepository extends CrudRepository<OrdemDeServico, Long> {


    OrdemDeServico findById(long id);


}
