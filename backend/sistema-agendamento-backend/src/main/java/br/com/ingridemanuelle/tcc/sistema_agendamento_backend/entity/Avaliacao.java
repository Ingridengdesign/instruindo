package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.enums.TipoAvaliador;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(exclude = { "aluno", "professor", "aula" })
@Entity
@Table(name = "avaliacoes")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAvaliacao;

    @Column(nullable = false)
    private Float nota;

    @Column(length = 1000)
    private String comentario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aluno", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_professor", nullable = false)
    private Professor professor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aula", nullable = false)
    private Aula aula;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAvaliador avaliador;
}