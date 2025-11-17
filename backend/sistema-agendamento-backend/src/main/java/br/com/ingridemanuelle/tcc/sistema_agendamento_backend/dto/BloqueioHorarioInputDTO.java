package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Objeto de transferência de dados para bloquear um horário na agenda")
public class BloqueioHorarioInputDTO {

    @NotNull(message = "A data/hora de início é obrigatória")
    @Future(message = "O bloqueio deve ser no futuro")
    @Schema(description = "Data e hora de início do bloqueio", example = "2024-12-31T10:00:00", required = true)
    private LocalDateTime dataHoraInicio;

    @NotNull(message = "A data/hora de fim é obrigatória")
    @Future(message = "O bloqueio deve ser no futuro")
    @Schema(description = "Data e hora de fim do bloqueio", example = "2024-12-31T12:00:00", required = true)
    private LocalDateTime dataHoraFim;

    @Schema(description = "Motivo do bloqueio", example = "Pausa para almoço")
    private String motivo;
}