package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.BloqueioHorario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Objeto de transferência de dados para exibir um bloqueio de horário")
public class BloqueioHorarioOutputDTO {

    @Schema(description = "ID do bloqueio", example = "1")
    private Long id;
    @Schema(description = "Data e hora de início do bloqueio", example = "2024-12-31T10:00:00")
    private LocalDateTime dataHoraInicio;
    @Schema(description = "Data e hora de fim do bloqueio", example = "2024-12-31T12:00:00")
    private LocalDateTime dataHoraFim;
    @Schema(description = "Motivo do bloqueio", example = "Pausa para almoço")
    private String motivo;

    public BloqueioHorarioOutputDTO(BloqueioHorario bloqueio) {
        this.id = bloqueio.getId();
        this.dataHoraInicio = bloqueio.getDataHoraInicio();
        this.dataHoraFim = bloqueio.getDataHoraFim();
        this.motivo = bloqueio.getMotivo();
    }
}