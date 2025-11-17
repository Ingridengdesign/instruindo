package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Mensagem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Objeto de transferência de dados para mensagens")
public class MensagemDTO {

    @Schema(description = "ID da mensagem", example = "1")
    private Long idMensagem;
    @Schema(description = "ID do remetente", example = "1")
    private Long idRemetente;
    @Schema(description = "Nome do remetente", example = "Aluno")
    private String nomeRemetente;
    @Schema(description = "ID do destinatário", example = "2")
    private Long idDestinatario;
    @Schema(description = "Nome do destinatário", example = "Professor")
    private String nomeDestinatario;
    @Schema(description = "Conteúdo da mensagem", example = "Olá, gostaria de agendar uma aula.")
    private String conteudo;
    @Schema(description = "Data de envio da mensagem")
    private LocalDateTime dataEnvio;
    @Schema(description = "Indica se a mensagem foi lida", example = "false")
    private boolean lida;

    public MensagemDTO(Mensagem mensagem) {
        this.idMensagem = mensagem.getIdMensagem();
        this.conteudo = mensagem.getConteudo();
        this.dataEnvio = mensagem.getDataEnvio();
        this.lida = mensagem.isLida();

        if (mensagem.getRemetente() != null) {
            this.idRemetente = mensagem.getRemetente().getIdUsuario();
            this.nomeRemetente = mensagem.getRemetente().getNome();
        }

        if (mensagem.getDestinatario() != null) {
            this.idDestinatario = mensagem.getDestinatario().getIdUsuario();
            this.nomeDestinatario = mensagem.getDestinatario().getNome();
        }
    }
}