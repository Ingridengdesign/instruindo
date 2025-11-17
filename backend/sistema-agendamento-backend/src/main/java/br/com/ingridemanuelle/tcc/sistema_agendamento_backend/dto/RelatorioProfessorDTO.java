package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor 
@Schema(description = "Objeto de transferência de dados para o relatório do professor")
public class RelatorioProfessorDTO {

    @Schema(description = "Data de início do relatório")
    private LocalDate dataInicio;
    @Schema(description = "Data de fim do relatório")
    private LocalDate dataFim;
    @Schema(description = "Total de aulas confirmadas no período", example = "10")
    private Long totalAulasConfirmadas;
    @Schema(description = "Faturamento estimado no período", example = "500.0")
    private Double faturamentoEstimado;

}