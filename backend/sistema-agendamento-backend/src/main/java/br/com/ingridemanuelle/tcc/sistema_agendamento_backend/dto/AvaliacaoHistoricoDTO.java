package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Avaliacao;
import lombok.Data;

@Data
public class AvaliacaoHistoricoDTO {

    private Float nota;
    private String comentario;
    private Long idAvaliacao;

    public AvaliacaoHistoricoDTO(Avaliacao avaliacao) {
        this.idAvaliacao = avaliacao.getIdAvaliacao(); // <-- CORRIGIDO
        this.nota = avaliacao.getNota();
        this.comentario = avaliacao.getComentario();
    }
}