package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.BloqueioHorario;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BloqueioHorarioRepository extends JpaRepository<BloqueioHorario, Long> {

    @Query("SELECT b FROM BloqueioHorario b " +
            "WHERE b.professor = :professor " +
            "AND b.dataHoraInicio < :fim " +
            "AND b.dataHoraFim > :inicio " +
            "ORDER BY b.dataHoraInicio ASC")
    List<BloqueioHorario> findBloqueiosNoIntervalo(
            @Param("professor") Professor professor,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

}