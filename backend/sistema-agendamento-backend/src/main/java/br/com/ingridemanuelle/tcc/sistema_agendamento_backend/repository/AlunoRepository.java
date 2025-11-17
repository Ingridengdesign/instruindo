package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    Optional<Aluno> findByEmail(String email);

    boolean existsByEmail(String email);

}
