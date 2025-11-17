package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Aluno;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Aula;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Avaliacao;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Professor;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.enums.TipoAvaliador; // Importar o Enum

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    @Query("SELECT COALESCE(AVG(a.nota), 0.0) FROM Avaliacao a WHERE a.professor.idUsuario = :idProfessor AND a.avaliador = 'ALUNO'")
    Float findAverageNotaByProfessor(@Param("idProfessor") Long idProfessor);

    List<Avaliacao> findByProfessor_IdUsuarioAndAvaliador(Long idProfessor, TipoAvaliador avaliador);

    boolean existsByAlunoAndAulaAndAvaliador(Aluno aluno, Aula aula, TipoAvaliador avaliador);

    boolean existsByProfessorAndAulaAndAvaliador(Professor professor, Aula aula, TipoAvaliador avaliador);

    Optional<Avaliacao> findByAlunoAndAulaAndAvaliador(Aluno aluno, Aula aula, TipoAvaliador avaliador);

    long countByProfessorAndAvaliador(Professor professor, TipoAvaliador avaliador);

}