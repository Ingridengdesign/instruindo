package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Schema(description = "Objeto de transferência de dados para solicitar uma aula")
public class SolicitacaoRequestDTO {

    @NotNull(message = "O ID do professor é obrigatório")
    @Schema(description = "ID do professor", example = "1", required = true)
    private Long idProfessor;

    @NotNull(message = "O ID da categoria é obrigatório")
    @Schema(description = "ID da categoria", example = "1", required = true)
    private Long idCategoria;

    @NotNull(message = "A data da solicitação é obrigatória")
    @Future(message = "A data da solicitação deve ser no futuro") 
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Data da solicitação da aula", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataSolicitacao;

    @Schema(description = "Detalhes da solicitação", example = "Gostaria de focar em derivadas.")
    private String detalhes; 
}