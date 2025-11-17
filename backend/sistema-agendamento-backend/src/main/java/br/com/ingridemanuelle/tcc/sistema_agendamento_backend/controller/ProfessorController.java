package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.controller;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.ProfessorCardDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.ProfessorPerfilDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service.DisponibilidadeService;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service.ProfessorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/professores")
@Tag(name = "Professores", description = "Endpoints para busca de professores")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private DisponibilidadeService disponibilidadeService;

    @GetMapping("/buscar")
    @Operation(summary = "Busca professores por filtros", description = "Busca professores por categoria, preço máximo e nota mínima")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Professores encontrados com sucesso") })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<ProfessorCardDTO>> buscarProfessores(
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(required = false) Double precoMax,
            @RequestParam(required = false) Double notaMin) {
        List<ProfessorCardDTO> professores = professorService.buscarProfessores(idCategoria, precoMax, notaMin);

        return ResponseEntity.ok(professores);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um professor por ID", description = "Retorna o perfil de um professor específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professor encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Professor não encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ProfessorPerfilDTO> getProfessorById(@PathVariable Long id) {
        try {
            ProfessorPerfilDTO perfil = professorService.buscarPerfilProfessor(id);
            return ResponseEntity.ok(perfil);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/horarios-disponiveis")
    @Operation(summary = "Ver os horários livres de um professor para um dia específico", description = "Calcula e retorna uma lista de strings de horários (ex: '14:00') que estão livres para agendamento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de horários retornada (pode ser vazia)"),
            @ApiResponse(responseCode = "404", description = "Professor não encontrado ou inativo")
    })
    public ResponseEntity<?> getHorariosDisponiveis(
            @Parameter(description = "ID do professor", required = true, example = "1") @PathVariable Long id,

            @Parameter(description = "Data a ser consultada (YYYY-MM-DD)", required = true, example = "2025-11-10") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        try {
            List<String> horarios = disponibilidadeService.getHorariosDisponiveis(id, data);
            return ResponseEntity.ok(horarios);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/dias-disponiveis")
    @Operation(summary = "Ver os dias com horários livres num mês específico", description = "Retorna uma lista de strings de datas (YYYY-MM-DD) que têm pelo menos um horário disponível.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de dias retornada (pode ser vazia)"),
            @ApiResponse(responseCode = "404", description = "Professor não encontrado ou inativo")
    })
    public ResponseEntity<?> getDiasDisponiveis(
            @Parameter(description = "ID do professor", required = true, example = "1") @PathVariable Long id,

            @Parameter(description = "Ano (YYYY)", required = true, example = "2025") @RequestParam int ano,

            @Parameter(description = "Mês (1-12)", required = true, example = "11") @RequestParam int mes) {
        try {
            List<String> dias = disponibilidadeService.getDiasDisponiveisNoMes(id, ano, mes);
            return ResponseEntity.ok(dias);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}