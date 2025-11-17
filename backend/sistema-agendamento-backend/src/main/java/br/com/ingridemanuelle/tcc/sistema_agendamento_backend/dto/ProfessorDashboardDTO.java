package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO com as estatísticas do dashboard do professor")
public class ProfessorDashboardDTO {

    @Schema(description = "Total de aulas alguma vez agendadas (Confirmadas, Realizadas, Canceladas)", example = "24")
    private Long solicitadas;

    @Schema(description = "Total de aulas marcadas como 'REALIZADO'", example = "18")
    private Long concluidas;

    @Schema(description = "Total de avaliações recebidas de alunos", example = "15")
    private Long avaliacoes;

    @Schema(description = "Número de solicitações de aula atualmente 'PENDENTE'", example = "3")
    private Long novosPedidos;
}