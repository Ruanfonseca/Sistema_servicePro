package com.ServicePro.ServicePro.repository;

import com.ServicePro.ServicePro.models.Funcionario;
import com.ServicePro.ServicePro.models.Requerimento;
import com.ServicePro.ServicePro.models.RequerimentoSala;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RequerimentoSalaRepository extends CrudRepository<RequerimentoSala, Long> {

	RequerimentoSala findByCodigo(long codigo);
	Iterable<RequerimentoSala> findByFuncionario(Funcionario funcionario);
	
	//List<Requerimento> findByNome(String nome);

	//Iterable<Requerimento> findByRequerimento(Requerimento requerimento);

	RequerimentoSala findByCpf(String cpf);
	@Query("SELECT r FROM RequerimentoSala r WHERE r.status = 'pendente'")
	Iterable<RequerimentoSala> findByStatusPendente();


	@Query("SELECT r FROM RequerimentoSala r WHERE r.status = 'finalizado'")
	Iterable<RequerimentoSala> findByStatusFinalizado();

	//Requerimento findById(long id);

	//List<Requerimento> findByNomeRequerente(String nomeRequerente);

	
	// Query para a busca
	@Query(value = "select u from RequerimentoSala u where u.cpf like %?1%")
	List<RequerimentoSala> findBycpf(String cpf);

}
