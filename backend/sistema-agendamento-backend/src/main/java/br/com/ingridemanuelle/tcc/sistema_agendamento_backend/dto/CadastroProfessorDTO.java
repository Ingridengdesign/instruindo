package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.util.Set;
import lombok.Data;

@Data
@Schema(description = "Objeto de transferência de dados para o cadastro de um professor")
public class CadastroProfessorDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Schema(description = "Nome do professor", example = "Alan Turing", required = true)
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    @Schema(description = "Email do professor", example = "professor@example.com", required = true)
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @Schema(description = "Senha do professor", example = "123456", required = true)
    private String senha;

    @NotEmpty(message = "Pelo menos uma categoria é obrigatória")
    @Schema(description = "Conjunto de IDs das categorias que o professor leciona", required = true)
    private Set<Long> idCategorias;

    @NotNull(message = "O preço por hora é obrigatório")
    @PositiveOrZero(message = "O preço não pode ser negativo")
    @Schema(description = "Preço por hora do professor", example = "50.0", required = true)
    private Double precoPorHora;
}