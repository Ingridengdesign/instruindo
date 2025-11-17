package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "Representa a agenda pública de um professor")
public class AgendaPublicaDTO {

    @Schema(description = "Lista de disponibilidades recorrentes do professor")
    private List<DisponibilidadeOutputDTO> disponibilidadeRecorrente;

    @Schema(description = "Lista de aulas já confirmadas do professor")
    private List<AulaAgendadaDTO> aulasConfirmadas;

    @Schema(description = "Lista de bloqueios de horários específicos do professor")
    private List<BloqueioHorarioOutputDTO> bloqueiosEspecificos;

    public AgendaPublicaDTO(
            List<DisponibilidadeOutputDTO> disponibilidadeRecorrente,
            List<AulaAgendadaDTO> aulasConfirmadas,
            List<BloqueioHorarioOutputDTO> bloqueiosEspecificos) {
        this.disponibilidadeRecorrente = disponibilidadeRecorrente;
        this.aulasConfirmadas = aulasConfirmadas;
        this.bloqueiosEspecificos = bloqueiosEspecificos;
    }
}