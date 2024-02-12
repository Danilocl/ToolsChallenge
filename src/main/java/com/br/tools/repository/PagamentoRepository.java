package com.br.tools.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.br.tools.model.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

    @Query("SELECT p FROM Pagamento p WHERE p.transacao.id = :id AND p.transacao.descricao.status = :status")
    Optional<Pagamento> findByIdAndStatus(String id, String status);

	@Query("SELECT p FROM Pagamento p WHERE p.transacao.descricao.status = :status")
	List<Pagamento> findByDescricaoStatus(String status);
	
	@Query("SELECT p FROM Pagamento p WHERE p.transacao.cartao = :cartao AND p.transacao.descricao.nsu = :nsu")
	Optional<Pagamento> findByCartaoNsu(String cartao,String nsu);

	@Query("SELECT p FROM Pagamento p WHERE p.transacao.id = :id")
	Optional<Pagamento> findById(String id);
	
	@Query("SELECT p FROM Pagamento p WHERE p.transacao.descricao.nsu = :nsu")
	Optional<Pagamento> findByNSU(String nsu);
}
