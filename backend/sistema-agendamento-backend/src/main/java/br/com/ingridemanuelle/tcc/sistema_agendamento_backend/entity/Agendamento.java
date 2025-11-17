package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@EqualsAndHashCode(exclude = { "aluno", "aula", "solicitacao" })
@Entity
@Table(name = "agendamentos")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAgendamento;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime dataAgendamento;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aluno", nullable = false)
    private Aluno aluno;

    @OneToOne
    @JoinColumn(name = "id_aula", nullable = false, unique = true)
    private Aula aula;

    @OneToOne
    @JoinColumn(name = "id_solicitacao", nullable = true, unique = true)
    private SolicitacaoAula solicitacao;
}