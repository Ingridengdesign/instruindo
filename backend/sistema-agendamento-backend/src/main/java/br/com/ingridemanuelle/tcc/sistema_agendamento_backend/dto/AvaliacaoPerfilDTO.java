package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Avaliacao;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Objeto de transferência de dados para avaliações")
public class AvaliacaoPerfilDTO {

    @Schema(description = "ID da avaliação", example = "1")
    private Long idAvaliacao;
    @Schema(description = "Nota da avaliação", example = "4.5")
    private Float nota;
    @Schema(description = "Comentário da avaliação", example = "Ótima aula!")
    private String comentario;
    @Schema(description = "Nome do aluno que fez a avaliação", example = "Aluno A")
    private String nomeAluno;

    public AvaliacaoPerfilDTO(Avaliacao avaliacao) {
        this.idAvaliacao = avaliacao.getIdAvaliacao();
        this.nota = avaliacao.getNota();
        this.comentario = avaliacao.getComentario();
        if (avaliacao.getAluno() != null) {
            this.nomeAluno = avaliacao.getAluno().getNome();
        } else {
            this.nomeAluno = "Anônimo";
        }
    }
}