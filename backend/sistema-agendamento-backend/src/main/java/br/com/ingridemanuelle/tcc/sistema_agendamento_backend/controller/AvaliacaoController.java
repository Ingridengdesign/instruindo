package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.controller;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.AvaliacaoRequestDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.ProfessorAvaliacaoAlunoDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service.AvaliacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/aulas/{idAula}/avaliacoes")
@Tag(name = "Avaliações", description = "Endpoints para gerenciamento de avaliações")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @PostMapping
    @Operation(summary = "Cria uma nova avaliação", description = "Cria uma nova avaliação para uma aula específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avaliação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> criarAvaliacao(
            @PathVariable Long idAula, @Valid @RequestBody AvaliacaoRequestDTO dto) {
        try {
            avaliacaoService.criarAvaliacao(idAula, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Avaliação criada com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/professor")
    @Operation(summary = "Professor avalia aluno", description = "Professor avalia um aluno para uma aula específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Aluno avaliado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> professorAvaliaAluno(
            @PathVariable Long idAula, @Valid @RequestBody ProfessorAvaliacaoAlunoDTO dto) {

        try {
            avaliacaoService.criarAvaliacaoProfessorParaAluno(idAula, dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Aluno avaliado com sucesso para esta aula.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}