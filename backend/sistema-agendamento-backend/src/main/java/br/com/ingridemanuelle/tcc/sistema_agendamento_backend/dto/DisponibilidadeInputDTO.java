package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.Data;

@Data
@Schema(description = "Objeto de transferência de dados para entrada de disponibilidade")
public class DisponibilidadeInputDTO {

    @NotNull(message = "O dia da semana é obrigatório")
    @Schema(description = "Dia da semana da disponibilidade", example = "MONDAY", required = true)
    private DayOfWeek diaDaSemana;

    @NotNull(message = "A hora de início é obrigatória")
    @Schema(description = "Hora de início da disponibilidade", example = "08:00:00", required = true)
    private LocalTime horaInicio;

    @NotNull(message = "A hora de fim é obrigatória")
    @Schema(description = "Hora de fim da disponibilidade", example = "12:00:00", required = true)
    private LocalTime horaFim;
}