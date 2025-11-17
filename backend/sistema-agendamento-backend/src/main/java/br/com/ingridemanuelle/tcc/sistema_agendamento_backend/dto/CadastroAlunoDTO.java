package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Objeto de transferência de dados para o cadastro de um aluno")
public class CadastroAlunoDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Schema(description = "Nome do aluno", example = "Aluno A", required = true)
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    @Schema(description = "Email do aluno", example = "aluno@example.com", required = true)
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @Schema(description = "Senha do aluno", example = "123456", required = true)
    private String senha;

}
