package com.ServicePro.ServicePro.repository;

import java.util.List;

import com.ServicePro.ServicePro.models.Auxiliar;
import com.ServicePro.ServicePro.models.Funcionario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AuxiliarRepository extends CrudRepository<Auxiliar, Long> {

	Iterable<Auxiliar> findByFuncionario(Funcionario funcionario);

	// para o método delete por CPF
	Auxiliar findByCpf(String cpf);
	
	Auxiliar findById(long id);
	List<Auxiliar> findByNome(String nome);

	// Query para a busca
	@Query(value = "select u from Auxiliar u where u.nome like %?1%")
	List<Auxiliar> findByNomesAuxiliares(String nome);

}