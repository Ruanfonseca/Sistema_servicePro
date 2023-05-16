package com.ServicePro.ServicePro.repository;

import com.ServicePro.ServicePro.models.Funcionario;
import com.ServicePro.ServicePro.models.Requerimento;
import com.ServicePro.ServicePro.models.RequerimentoProjetor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RequerimentoProjetorRepository extends CrudRepository<RequerimentoProjetor, Long> {

	RequerimentoProjetor findByCodigo(long codigo);
	Iterable<RequerimentoProjetor> findByFuncionario(Funcionario funcionario);
	
	//List<Requerimento> findByNome(String nome);

	//Iterable<Requerimento> findByRequerimento(Requerimento requerimento);

	RequerimentoProjetor findByCpf(String cpf);
	@Query("SELECT r FROM RequerimentoProjetor r WHERE r.status = 'pendente'")
	Iterable<RequerimentoProjetor> findByStatusPendente();


	@Query("SELECT r FROM Requerimento r WHERE r.status = 'finalizado'")
	Iterable<Requerimento> findByStatusFinalizado();

	//Requerimento findById(long id);

	//List<Requerimento> findByNomeRequerente(String nomeRequerente);

	
	// Query para a busca
	@Query(value = "select u from Requerimento u where u.cpf like %?1%")
	List<Requerimento> findBycpf(String cpf);

}
