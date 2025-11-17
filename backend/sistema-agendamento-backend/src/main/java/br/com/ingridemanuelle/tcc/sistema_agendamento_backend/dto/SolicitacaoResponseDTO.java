package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.SolicitacaoAula;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Schema(description = "Objeto de transferência de dados para resposta de solicitação de aula")
public class SolicitacaoResponseDTO {

  @Schema(description = "ID da solicitação", example = "1")
  private Long idSolicitacao;

  @Schema(description = "Data da solicitação da aula")
  private LocalDateTime dataSolicitacao;

  @Schema(description = "Detalhes da solicitação", example = "Gostaria de focar em derivadas.")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private String detalhes;

  @Schema(description = "Status da solicitação", example = "PENDENTE")
  private String status;

  @Schema(description = "ID do aluno", example = "1")
  private Long idAluno;

  @Schema(description = "Nome do aluno", example = "Ingrid Emanuelle")
  private String nomeAluno;

  @Schema(description = "Email do aluno", example = "ingrid.emanuelle@example.com")
  private String emailAluno;

  private Long idProfessor;
  private String nomeProfessor;
  private String localAula;

  public SolicitacaoResponseDTO(SolicitacaoAula solicitacao) {
    this.idSolicitacao = solicitacao.getIdSolicitacao();
    this.dataSolicitacao = solicitacao.getDataSolicitacao();
    this.detalhes = solicitacao.getDetalhes();
    this.status = solicitacao.getStatus();

    if (solicitacao.getAluno() != null) {
      this.idAluno = solicitacao.getAluno().getIdUsuario();
    }

    if (solicitacao.getProfessor() != null) {
      this.idProfessor = solicitacao.getProfessor().getIdUsuario();
      this.nomeProfessor = solicitacao.getProfessor().getNome();
    }
    
    if (solicitacao.getAgendamento() != null && solicitacao.getAgendamento().getAula() != null) {
      this.localAula = solicitacao.getAgendamento().getAula().getLocal();
    }
  }
}