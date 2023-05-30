package com.ServicePro.ServicePro.repository;

import java.util.List;

import com.ServicePro.ServicePro.models.Auxiliar;
import com.ServicePro.ServicePro.models.Funcionario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface FuncionarioRepository extends CrudRepository<Funcionario, Long>{
	
	Funcionario findById(long id);

	Funcionario findByNome(String nome);


	// Query para a busca
	@Query(value = "select u from Funcionario u where u.nome like %?1%")
	List<Funcionario>findByNomes(String nome);

	@Query(value = "select u from Funcionario u where u.Tipo ='COINFO'")
	Iterable<Funcionario> findAllsetor(String setor);

	Auxiliar findByCpf(String cpf);
	Page<Funcionario> findAll(Pageable pageable);


	@Query("select u from Funcionario u where u.matricula = ?1")
	Funcionario findByMatricula(String matricula);


	@Query("SELECT f FROM Funcionario f")
	Page<Funcionario> findall(Pageable pageable);

}