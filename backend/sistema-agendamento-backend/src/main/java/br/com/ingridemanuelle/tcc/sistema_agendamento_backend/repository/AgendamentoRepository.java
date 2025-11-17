package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Agendamento;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Aluno;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Aula;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Professor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    Optional<Agendamento> findByAula(Aula aula);

    boolean existsByAlunoAndAulaAndStatusIn(Aluno aluno, Aula aula, List<String> status);

    List<Agendamento> findByAula_ProfessorAndStatus(Professor professor, String status);

    List<Agendamento> findByAula_ProfessorOrderByAula_DataHoraDesc(Professor professor);

    List<Agendamento> findByAlunoOrderByAula_DataHoraDesc(Aluno aluno);

    long countByAula_Professor(Professor professor);

    long countByAula_ProfessorAndStatus(Professor professor, String status);

    @Query("SELECT COUNT(ag) > 0 FROM Agendamento ag " +
            "WHERE ag.aula.professor = :professor " +
            "AND ag.status = 'CONFIRMADO' " +
            "AND ag.aula.dataHora = :dataHoraDesejada")
    boolean existsConflitoDeHorario(
            @Param("professor") Professor professor,
            @Param("dataHoraDesejada") LocalDateTime dataHoraDesejada);

    @Query("SELECT COUNT(ag) > 0 FROM Agendamento ag " +
            "WHERE ag.aula.professor = :professor " +
            "AND ag.status = 'CONFIRMADO' " +
            "AND ag.aula.dataHora < :fimBloqueio " +
            "AND ag.aula.dataHoraFim > :inicioBloqueio")
    boolean existsConflitoDeHorarioEmIntervalo(
            @Param("professor") Professor professor,
            @Param("inicioBloqueio") LocalDateTime inicioBloqueio,
            @Param("fimBloqueio") LocalDateTime fimBloqueio);

    @Query("SELECT COUNT(ag) FROM Agendamento ag " +
            "WHERE ag.aula.professor = :professor " +
            "AND ag.status = 'CONFIRMADO' " +
            "AND ag.aula.dataHora BETWEEN :inicio AND :fim")
    Long countAgendamentosConfirmadosPeriodo(
            @Param("professor") Professor professor,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    @Query("SELECT COALESCE(SUM(a.preco), 0.0) FROM Agendamento ag " +
            "JOIN ag.aula a " +
            "WHERE a.professor = :professor " +
            "AND ag.status = 'CONFIRMADO' " +
            "AND a.dataHora BETWEEN :inicio AND :fim")
    Double sumFaturamentoConfirmadoPeriodo(
            @Param("professor") Professor professor,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    @Query("SELECT ag FROM Agendamento ag " +
            "WHERE ag.aula.professor = :professor " +
            "AND ag.status = 'CONFIRMADO' " +
            "AND ag.aula.dataHora >= :inicio " +
            "AND ag.aula.dataHora < :fim")
    List<Agendamento> findAgendamentosConfirmadosEntre(
            @Param("professor") Professor professor,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    @Query("SELECT ag FROM Agendamento ag " +
            "JOIN FETCH ag.aluno " +
            "JOIN FETCH ag.aula a " +
            "LEFT JOIN FETCH a.avaliacoes " +
            "WHERE ag.aluno = :aluno " +
            "ORDER BY a.dataHora Desc")
    List<Agendamento> findHistoricoCompletoByAluno(@Param("aluno") Aluno aluno);

}