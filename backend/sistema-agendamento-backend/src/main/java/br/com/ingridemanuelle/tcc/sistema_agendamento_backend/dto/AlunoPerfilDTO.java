package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Aluno;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Objeto de transferência de dados para o perfil do aluno")
public class AlunoPerfilDTO {

    @Schema(description = "ID do aluno", example = "1")
    private Long id;
    @Schema(description = "Nome do aluno", example = "Aluno A")
    private String nome;
    @Schema(description = "Email do aluno", example = "aluno@example.com")
    private String email;
    @Schema(description = "Data de cadastro do aluno")
    private LocalDate dataCadastro;

    private String fotoUrl;

    public AlunoPerfilDTO(Aluno aluno) {
        this.id = aluno.getIdUsuario();
        this.nome = aluno.getNome();
        this.email = aluno.getEmail();
        this.dataCadastro = aluno.getDataCadastro();
        this.fotoUrl = aluno.getFotoUrl();

    }
}