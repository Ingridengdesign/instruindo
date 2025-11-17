package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = { "solicitacoes", "avaliacoes", "agendamentos" })
@Entity
@Table(name = "alunos")
public class Aluno extends Usuario {

    @Override
    public String getRole() {
        return "ROLE_ALUNO";
    }

    @OneToMany(mappedBy = "aluno")
    private Set<Avaliacao> avaliacoes;

    @OneToMany(mappedBy = "aluno")
    private Set<SolicitacaoAula> solicitacoes;

    @OneToMany(mappedBy = "aluno")
    private Set<Agendamento> agendamentos;
}
