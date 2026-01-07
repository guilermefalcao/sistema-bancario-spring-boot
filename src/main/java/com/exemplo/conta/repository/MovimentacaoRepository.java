package com.exemplo.conta.repository;

import com.exemplo.conta.entity.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository JPA para Movimentacao
 * Fornece operações CRUD e consultas específicas
 */
@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    
    /**
     * Busca todas as movimentações de uma conta ordenadas por data (mais recente primeiro)
     */
    @Query("SELECT m FROM Movimentacao m WHERE m.idConta = :idConta ORDER BY m.dataMovimentacao DESC")
    List<Movimentacao> findByIdContaOrderByDataMovimentacaoDesc(@Param("idConta") Long idConta);
    
    /**
     * Busca últimas N movimentações de uma conta
     */
    @Query(value = "SELECT * FROM MOVIMENTACAO WHERE ID_CONTA = :idConta ORDER BY DATA_MOV DESC FETCH FIRST :limite ROWS ONLY", nativeQuery = true)
    List<Movimentacao> findUltimasMovimentacoes(@Param("idConta") Long idConta, @Param("limite") int limite);
    
    /**
     * Exclui todas as movimentações de uma conta
     */
    void deleteByIdConta(Long idConta);
}