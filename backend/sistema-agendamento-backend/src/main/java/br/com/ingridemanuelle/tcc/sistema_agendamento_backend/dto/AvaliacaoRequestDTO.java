package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Objeto de transferência de dados para solicitar uma avaliação")
public class AvaliacaoRequestDTO {

    @NotNull(message = "A nota é obrigatória")
    @Min(value = 1, message = "A nota mínima é 1")
    @Max(value = 5, message = "A nota máxima é 5")
    @Schema(description = "Nota da avaliação", example = "4.5", required = true)
    private Float nota;

    @Schema(description = "Comentário da avaliação", example = "Ótima aula!")
    private String comentario;
}