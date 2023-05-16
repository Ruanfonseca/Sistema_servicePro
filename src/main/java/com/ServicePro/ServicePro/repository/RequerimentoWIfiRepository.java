package com.ServicePro.ServicePro.repository;

import java.util.List;

import com.ServicePro.ServicePro.models.Requerimento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ServicePro.ServicePro.models.Funcionario;

public interface RequerimentoWIfiRepository extends CrudRepository<Requerimento, Long> {
	
	Requerimento findByCodigo(long codigo);
	Iterable<Requerimento> findByFuncionario(Funcionario funcionario);
	
	//List<Requerimento> findByNome(String nome);

	//Iterable<Requerimento> findByRequerimento(Requerimento requerimento);

	Requerimento findByCpf(String cpf);
	@Query("SELECT r FROM Requerimento r WHERE r.status = 'PENDENTE'")
	Iterable<Requerimento> findByStatusPendente();


	@Query("SELECT r FROM Requerimento r WHERE r.status = 'FINALIZADO'")
	Iterable<Requerimento> findByStatusFinalizado();

	//Requerimento findById(long id);

	//List<Requerimento> findByNomeRequerente(String nomeRequerente);

	
	// Query para a busca
	@Query(value = "select u from Requerimento u where u.cpf like %?1%")
	List<Requerimento> findBycpf(String cpf);

}
