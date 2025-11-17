package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Objeto de transferência de dados para resetar a senha")
public class ResetarSenhaDTO {
    @NotBlank(message = "O token é obrigatório.")
    @Schema(description = "Token de reset de senha", required = true)
    private String token;

    @NotBlank(message = "A nova senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    @Schema(description = "Nova senha do usuário", example = "newPassword123", required = true)
    private String novaSenha;
}