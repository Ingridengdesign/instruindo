package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@EqualsAndHashCode(exclude = { "aluno", "professor" })
@Entity
@Table(name = "solicitacoes_aula")
public class SolicitacaoAula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSolicitacao;

    @Temporal(TemporalType.TIMESTAMP)

    @Column(nullable = false)
    private LocalDateTime dataSolicitacao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime getDataSolicitacao() {
        return this.dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    @Column(length = 500)
    private String detalhes;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aluno", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_professor", nullable = false)
    private Professor professor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @OneToOne(mappedBy = "solicitacao")
    private Agendamento agendamento;

}