package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = { "remetente", "destinatario" })
@Entity
@Table(name = "mensagens")
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMensagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_remetente", nullable = false)
    private Usuario remetente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_destinatario", nullable = false)
    private Usuario destinatario;

    @Column(nullable = false, length = 2000)
    private String conteudo;

    @Column(nullable = false)
    private LocalDateTime dataEnvio;

    @Column(nullable = false)
    private boolean lida = false;

    public Mensagem() {
        this.dataEnvio = LocalDateTime.now();
        this.lida = false;
    }
}