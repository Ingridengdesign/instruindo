package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Disponibilidade;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Schema(description = "Objeto de transferência de dados para saída de disponibilidade")
public class DisponibilidadeOutputDTO {

    @Schema(description = "ID da disponibilidade", example = "1")
    private Long idDisponibilidade;
    @Schema(description = "Dia da semana da disponibilidade", example = "MONDAY")
    private DayOfWeek diaDaSemana;
    @Schema(description = "Hora de início da disponibilidade", example = "08:00:00")
    private LocalTime horaInicio;
    @Schema(description = "Hora de fim da disponibilidade", example = "12:00:00")
    private LocalTime horaFim;

    public DisponibilidadeOutputDTO(Disponibilidade disponibilidade) {
        this.idDisponibilidade = disponibilidade.getIdDisponibilidade();
        this.diaDaSemana = disponibilidade.getDiaDaSemana();
        this.horaInicio = disponibilidade.getHoraInicio();
        this.horaFim = disponibilidade.getHoraFim();
    }
}