package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Objeto de transferência de dados para criar uma nova mensagem")
public class NovaMensagemDTO {

    @NotNull(message = "O ID do destinatário é obrigatório.")
    @Schema(description = "ID do destinatário", example = "2", required = true)
    private Long idDestinatario;

    @NotBlank(message = "O conteúdo da mensagem não pode ser vazio.")
    @Size(max = 2000, message = "Mensagem muito longa.")
    @Schema(description = "Conteúdo da mensagem", example = "Olá, gostaria de agendar uma aula.", required = true)
    private String conteudo;
}