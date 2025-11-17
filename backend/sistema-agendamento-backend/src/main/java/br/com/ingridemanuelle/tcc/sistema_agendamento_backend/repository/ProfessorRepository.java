package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    Optional<Professor> findByIdUsuarioAndAtivoTrue(Long id);

    @Query("SELECT p FROM Professor p " +
            "LEFT JOIN p.categorias c " +
            "LEFT JOIN p.avaliacoes a " +
            "WHERE p.ativo = true " +
            "AND (:idCategoria IS NULL OR c.idCategoria = :idCategoria) " +
            "AND (:precoMax IS NULL OR p.precoPorHora <= :precoMax) " +
            "GROUP BY p.idUsuario " +
            "HAVING (:notaMin IS NULL OR COALESCE(AVG(a.nota), 0.0) >= :notaMin)")
    List<Professor> searchProfessores(
            @Param("idCategoria") Long idCategoria,
            @Param("precoMax") Double precoMax,
            @Param("notaMin") Double notaMin);
}