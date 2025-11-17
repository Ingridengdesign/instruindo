package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.controller;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.AcaoSolicitacaoDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.SolicitacaoRequestDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.SolicitacaoResponseDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service.SolicitacaoAulaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solicitacoes")
@Tag(name = "Solicitações de Aula", description = "Endpoints para gerenciamento de solicitações de aula")
@SecurityRequirement(name = "bearerAuth")
public class SolicitacaoAulaController {

    @Autowired
    private SolicitacaoAulaService solicitacaoService;

    @PostMapping
    @Operation(summary = "Cria uma nova solicitação de aula", description = "Envia uma nova solicitação de aula para um professor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitação de aula enviada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> criarSolicitacao(@Valid @RequestBody SolicitacaoRequestDTO dto) {
        try {
            solicitacaoService.criarSolicitacao(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Solicitação de aula enviada com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/professor")
    @Operation(summary = "Busca as solicitações para o professor", description = "Retorna a lista de solicitações de aula para o professor autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitações encontradas com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getSolicitacoesParaProfessor() {
        try {
            List<SolicitacaoResponseDTO> solicitacoes = solicitacaoService.buscarSolicitacoesPorProfessor();
            return ResponseEntity.ok(solicitacoes);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/responder")
    @Operation(summary = "Responde a uma solicitação de aula", description = "Aceita ou recusa uma solicitação de aula")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitação respondida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> responderSolicitacao(
            @PathVariable Long id, @Valid @RequestBody AcaoSolicitacaoDTO dto) {

        try {
            solicitacaoService.responderSolicitacao(id, dto);
            return ResponseEntity.ok().body("Solicitação respondida com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/aluno")
    @Operation(summary = "Aluno lista suas solicitações de aula", description = "Retorna o histórico de solicitações enviadas e o status atual (PENDENTE, ACEITA, RECUSADA).")
    public ResponseEntity<?> getMinhasSolicitacoes() {
        try {
            List<SolicitacaoResponseDTO> solicitacoes = solicitacaoService.buscarMinhasSolicitacoes();
            return ResponseEntity.ok(solicitacoes);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}