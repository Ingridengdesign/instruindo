package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Agendamento;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Avaliacao;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "DTO para representar uma aula agendada")
public class AulaAgendadaDTO {

    @Schema(description = "ID do agendamento")
    private Long idAgendamento;

    @Schema(description = "ID da aula")
    private Long idAula;

    @Schema(description = "Título da aula")
    private String tituloAula;

    @Schema(description = "Data e hora da aula")
    private LocalDateTime dataHoraAula;

    @Schema(description = "Descrição da aula")
    private String descricaoAula;

    @Schema(description = "Local da aula")
    private String localAula;

    @Schema(description = "ID do aluno")
    private Long idAluno;

    @Schema(description = "Nome do aluno")
    private String nomeAluno;

    @Schema(description = "Email do aluno")
    private String emailAluno;

    @Schema(description = "Status do agendamento")
    private String statusAgendamento;

    @Schema(description = "Avaliação do aluno")
    private AvaliacaoHistoricoDTO avaliacaoAluno;

    public AulaAgendadaDTO(Agendamento agendamento, Avaliacao avaliacaoDoAluno) {
        this.idAgendamento = agendamento.getIdAgendamento();
        this.statusAgendamento = agendamento.getStatus();

        if (agendamento.getAula() != null) {
            this.idAula = agendamento.getAula().getIdAula();
            this.tituloAula = agendamento.getAula().getTitulo();
            this.dataHoraAula = agendamento.getAula().getDataHora();
            this.descricaoAula = agendamento.getAula().getDescricao();
            this.localAula = agendamento.getAula().getLocal();
        }

        if (agendamento.getAluno() != null) {
            this.idAluno = agendamento.getAluno().getIdUsuario();
            this.nomeAluno = agendamento.getAluno().getNome();
            this.emailAluno = agendamento.getAluno().getEmail();
        }

        if (avaliacaoDoAluno != null) {
            this.avaliacaoAluno = new AvaliacaoHistoricoDTO(avaliacaoDoAluno);
        } else {
            this.avaliacaoAluno = null;
        }
    }
}
