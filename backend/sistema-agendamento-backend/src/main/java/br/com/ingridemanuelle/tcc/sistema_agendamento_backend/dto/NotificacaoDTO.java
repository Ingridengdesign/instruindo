package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Notificacao;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Objeto de transferência de dados para notificações")
public class NotificacaoDTO {

    @Schema(description = "ID da notificação", example = "1")
    private Long id;
    @Schema(description = "Mensagem da notificação", example = "Nova solicitação de aula")
    private String mensagem;
    @Schema(description = "Data de criação da notificação")
    private LocalDateTime dataCriacao;
    @Schema(description = "Indica se a notificação foi lida", example = "false")
    private boolean lida;
    @Schema(description = "Tipo da notificação", example = "SOLICITACAO_AULA")
    private String tipo;
    @Schema(description = "Link para a notificação", example = "/solicitacoes/1")
    private String link;

    public NotificacaoDTO(Notificacao notificacao) {
        this.id = notificacao.getId();
        this.mensagem = notificacao.getMensagem();
        this.dataCriacao = notificacao.getDataCriacao();
        this.lida = notificacao.isLida();
        this.tipo = notificacao.getTipo();
        this.link = notificacao.getLink();
    }
}