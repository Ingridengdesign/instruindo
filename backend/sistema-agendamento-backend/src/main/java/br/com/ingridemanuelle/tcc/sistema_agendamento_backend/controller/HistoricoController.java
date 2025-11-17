package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.controller;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.AulaAgendadaDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/historico")
@Tag(name = "Histórico", description = "Endpoints para visualização de histórico de aulas")
public class HistoricoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @GetMapping("/aluno")
    @Operation(summary = "Busca o histórico de aulas do aluno logado", description = "Retorna o histórico de aulas do aluno autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico encontrado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getHistoricoAluno() {
        try {
            List<AulaAgendadaDTO> historico = agendamentoService.buscarHistoricoAlunoLogado();
            return ResponseEntity.ok(historico);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @GetMapping("/professor")
    @Operation(summary = "Busca o histórico de aulas do professor logado", description = "Retorna o histórico de aulas do professor autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico encontrado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getHistoricoProfessor() {
        try {
            List<AulaAgendadaDTO> historico = agendamentoService.buscarHistoricoProfessorLogado();
            return ResponseEntity.ok(historico);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}