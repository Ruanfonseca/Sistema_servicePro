package com.ServicePro.ServicePro.repository;

import java.util.List;

import com.ServicePro.ServicePro.models.Requerimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ServicePro.ServicePro.models.Funcionario;

public interface RequerimentoWIfiRepository extends CrudRepository<Requerimento, Long> {
	
	Requerimento findByCodigo(long codigo);
	Iterable<Requerimento> findByFuncionario(Funcionario funcionario);
	
	//List<Requerimento> findByNome(String nome);

	//Iterable<Requerimento> findByRequerimento(Requerimento requerimento);

	Requerimento findByCpf(String cpf);
	@Query("SELECT r FROM Requerimento r WHERE r.status = 'FINALIZADO'")
	Iterable<Requerimento> findByStatusFinalizado();

	//faça uma busca no banco de dados e retorne por exemplo na pagina 0 10
	// requerimentos finalizados
	@Query("SELECT r FROM Requerimento r WHERE r.status = 'FINALIZADO'")
	Page<Requerimento> findByStatusFinalizado(Pageable pageable);

	@Query("SELECT r FROM Requerimento r WHERE r.status = 'PENDENTE'")
	Page<Requerimento> findByStatusPendente(Pageable pageable);

	
	// Query para a busca
	@Query(value = "select u from Requerimento u where u.cpf like %?1%")
	List<Requerimento> findBycpf(String cpf);

}
