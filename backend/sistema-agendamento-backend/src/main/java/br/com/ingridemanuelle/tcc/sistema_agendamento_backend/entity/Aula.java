package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(exclude = { "professor", "categoria", "agendamento", "avaliacoes" })
@Entity
@Table(name = "aulas")
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAula;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 500)
    private String descricao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private LocalDateTime dataHoraFim;

    private String local;

    private Double preco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_professor", nullable = false)
    private Professor professor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @OneToOne(mappedBy = "aula")
    private Agendamento agendamento;

    @OneToMany(mappedBy = "aula")
    private Set<Avaliacao> avaliacoes;
}