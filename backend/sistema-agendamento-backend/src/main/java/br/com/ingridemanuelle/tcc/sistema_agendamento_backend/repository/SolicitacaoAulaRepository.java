package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Aluno;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Professor;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.SolicitacaoAula;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitacaoAulaRepository extends JpaRepository<SolicitacaoAula, Long> {

    List<SolicitacaoAula> findByProfessor(Professor professor);

    List<SolicitacaoAula> findByAlunoOrderByDataSolicitacaoDesc(Aluno aluno);

    long countByProfessorAndStatus(Professor professor, String status);
}