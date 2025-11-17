package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Objeto de transferência de dados para solicitação de login")
public class LoginRequestDTO {

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    @Schema(description = "Email do usuário", example = "aluno@example.com", required = true)
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Schema(description = "Senha do usuário", example = "123456", required = true)
    private String senha;

}
