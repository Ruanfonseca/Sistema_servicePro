package com.ServicePro.ServicePro.repository;

import com.ServicePro.ServicePro.models.Funcionario;
import com.ServicePro.ServicePro.models.Requerimento;
import com.ServicePro.ServicePro.models.RequerimentoProjetor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RequerimentoProjetorRepository extends CrudRepository<RequerimentoProjetor, Long> {

	RequerimentoProjetor findByCodigo(long codigo);
	Iterable<RequerimentoProjetor> findByFuncionario(Funcionario funcionario);
	
	//List<Requerimento> findByNome(String nome);

	//Iterable<Requerimento> findByRequerimento(Requerimento requerimento);

	RequerimentoProjetor findByCpf(String cpf);



	//faça uma busca no banco de dados e retorne por exemplo na pagina 0 10
	// requerimentos finalizados
	@Query("SELECT r FROM RequerimentoProjetor r WHERE r.status = 'FINALIZADO'")
	Page<RequerimentoProjetor> findByStatusFinalizado(Pageable pageable);

	@Query("SELECT r FROM RequerimentoProjetor r WHERE r.status = 'PENDENTE'")
	Page<RequerimentoProjetor> findByStatusPendente(Pageable pageable);


	//Requerimento findById(long id);

	//List<Requerimento> findByNomeRequerente(String nomeRequerente);

	
	// Query para a busca
	@Query(value = "select u from Requerimento u where u.cpf like %?1%")
	List<Requerimento> findBycpf(String cpf);

}
