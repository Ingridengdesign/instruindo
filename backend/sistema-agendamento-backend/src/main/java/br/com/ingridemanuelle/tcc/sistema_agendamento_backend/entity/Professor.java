package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity;

import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = { "categorias", "avaliacoes", "solicitacoes", "disponibilidades" })
@Entity
@Table(name = "professores")
public class Professor extends Usuario {

    @Override
    public String getRole() {
        return "ROLE_PROFESSOR";
    }

    private Double precoPorHora;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "professor_categorias", joinColumns = @JoinColumn(name = "id_professor"), inverseJoinColumns = @JoinColumn(name = "id_categoria"))
    private Set<Categoria> categorias;

    @OneToMany(mappedBy = "professor")
    private Set<Avaliacao> avaliacoes;

    @OneToMany(mappedBy = "professor")
    private Set<SolicitacaoAula> solicitacoes;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Disponibilidade> disponibilidades;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BloqueioHorario> bloqueios;

}
