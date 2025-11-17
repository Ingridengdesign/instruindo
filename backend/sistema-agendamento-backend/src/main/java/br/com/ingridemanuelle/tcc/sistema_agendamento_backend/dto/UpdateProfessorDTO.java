package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Objeto de transferência de dados para atualizar um professor")
public class UpdateProfessorDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Schema(description = "Nome do professor", example = "Alan Turing", required = true)
    private String nome;

    @NotEmpty(message = "Pelo menos uma categoria é obrigatória")
    @Schema(description = "Conjunto de IDs das categorias que o professor leciona", required = true)
    private Set<Long> idCategorias;

    @NotNull(message = "O preço por hora é obrigatório")
    @PositiveOrZero(message = "O preço não pode ser negativo")
    @Schema(description = "Preço por hora do professor", example = "50.0", required = true)
    private Double precoPorHora;

    @Schema(description = "Nova senha do professor", example = "newPassword123")
    private String novaSenha;
}