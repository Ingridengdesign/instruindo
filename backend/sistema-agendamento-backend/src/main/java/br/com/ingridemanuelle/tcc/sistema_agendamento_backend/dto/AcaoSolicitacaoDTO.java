package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Schema(description = "Objeto de transferência de dados para ações de solicitação de aula")
public class AcaoSolicitacaoDTO {

    @NotBlank(message = "A ação é obrigatória.")
    @Schema(description = "Ação a ser realizada na solicitação", example = "ACEITAR")
    private String acao;

    @Schema(description = "O local da aula (ex: 'Sala 10' ou 'https://meet.google.com/xyz')", example = "https://meet.google.com/xyz")
    private String local;

}
