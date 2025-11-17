package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Professor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Schema(description = "Objeto de transferência de dados para o card do professor")
public class ProfessorCardDTO {

    @Schema(description = "ID do professor", example = "1")
    private Long idProfessor;
    @Schema(description = "Nome do professor", example = "Alan Turing")
    private String nome;
    @Schema(description = "Preço por hora do professor", example = "50.0")
    private Double precoPorHora;

    @Schema(description = "Categorias que o professor leciona")
    private Set<String> categorias;
    @Schema(description = "Média de avaliação do professor", example = "4.8")
    private Double mediaAvaliacao;

    public ProfessorCardDTO(Professor professor) {
        this.idProfessor = professor.getIdUsuario();
        this.nome = professor.getNome();
        this.precoPorHora = professor.getPrecoPorHora();

        if (professor.getCategorias() != null) {
            this.categorias = professor.getCategorias().stream()
                    .map(categoria -> categoria.getTitulo())
                    .collect(Collectors.toSet());
        }

        if (professor.getAvaliacoes() != null && !professor.getAvaliacoes().isEmpty()) {

            double media = professor.getAvaliacoes().stream()
                    .mapToDouble(avaliacao -> avaliacao.getNota().doubleValue())
                    .average()
                    .orElse(0.0);
            this.mediaAvaliacao = Math.round(media * 10.0) / 10.0;
        } else {
            this.mediaAvaliacao = 0.0;
        }
    }
}