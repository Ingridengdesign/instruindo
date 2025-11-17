package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO simplificado para listar Categorias (disciplinas)")
public class CategoriaDTO {

    @Schema(example = "1")
    private Long idCategoria;

    @Schema(example = "Matemática")
    private String nome;

    public CategoriaDTO(Categoria categoria) {
        this.idCategoria = categoria.getIdCategoria();
        this.nome = categoria.getTitulo();
    }
}