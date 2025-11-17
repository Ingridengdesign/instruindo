package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "usuarioDestino")
@Entity
@Table(name = "notificacoes")
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_destino", nullable = false)
    private Usuario usuarioDestino;

    @Column(nullable = false, length = 500)
    private String mensagem;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private boolean lida = false;

    @Column(length = 50)
    private String tipo;

    @Column(length = 255)
    private String link;

    public Notificacao() {
        this.dataCriacao = LocalDateTime.now();
        this.lida = false;
    }

    public Notificacao(Usuario usuarioDestino, String mensagem, String tipo, String link) {
        this();
        this.usuarioDestino = usuarioDestino;
        this.mensagem = mensagem;
        this.tipo = tipo;
        this.link = link;
    }
}