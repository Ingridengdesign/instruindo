package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Objeto de transferência de dados para atualizar um aluno")
public class UpdateAlunoDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Schema(description = "Nome do aluno", example = "Aluno", required = true)
    private String nome;

    @Schema(description = "Nova senha do aluno", example = "newPassword123")
    private String novaSenha;
}