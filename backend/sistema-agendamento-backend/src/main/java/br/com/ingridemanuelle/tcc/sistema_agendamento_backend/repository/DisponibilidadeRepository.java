package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Disponibilidade;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface DisponibilidadeRepository extends JpaRepository<Disponibilidade, Long> {

    List<Disponibilidade> findByProfessor(Professor professor);

    void deleteByProfessor(Professor professor);

    Optional<Disponibilidade> findByProfessorAndDiaDaSemana(Professor professor, DayOfWeek diaDaSemana);
}