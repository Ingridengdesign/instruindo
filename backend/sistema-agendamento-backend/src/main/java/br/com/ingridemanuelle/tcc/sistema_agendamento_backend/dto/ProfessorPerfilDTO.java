package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Professor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true) 
@Schema(description = "Objeto de transferência de dados para o perfil do professor")
public class ProfessorPerfilDTO extends ProfessorCardDTO {

    @Schema(description = "Email do professor", example = "professor@example.com")
    private String email; 
    @Schema(description = "Avaliações do professor")
    private Set<AvaliacaoPerfilDTO> avaliacoes;
    
    private String fotoUrl;

    public ProfessorPerfilDTO(Professor professor) {
  
        super(professor); 

        this.email = professor.getEmail();
        this.fotoUrl = professor.getFotoUrl();
        
        if (professor.getAvaliacoes() != null) {
            this.avaliacoes = professor.getAvaliacoes().stream()
                    .map(avaliacao -> new AvaliacaoPerfilDTO(avaliacao)) 
                    .collect(Collectors.toSet());
        }
    }
}