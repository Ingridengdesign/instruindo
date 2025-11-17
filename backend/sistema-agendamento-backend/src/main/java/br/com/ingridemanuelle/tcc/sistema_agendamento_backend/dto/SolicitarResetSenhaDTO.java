package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Objeto de transferência de dados para solicitar o reset de senha")
public class SolicitarResetSenhaDTO {
    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Formato de e-mail inválido.")
    @Schema(description = "Email do usuário para qual o reset de senha será enviado", example = "teste@example.com", required = true)
    private String email;
}