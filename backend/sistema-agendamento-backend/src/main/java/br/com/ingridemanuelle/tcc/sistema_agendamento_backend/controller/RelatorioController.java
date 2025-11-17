package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.controller;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.ProfessorDashboardDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.RelatorioProfessorDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relatorios/professor")
@Tag(name = "Relatórios", description = "Endpoints para geração de relatórios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/agendamentos")
    @Operation(summary = "Gera um relatório de agendamentos para o professor", description = "Gera um relatório de agendamentos para o professor autenticado dentro de um período")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getRelatorioAgendamentos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        try {
            RelatorioProfessorDTO relatorio = relatorioService.gerarRelatorioProfessorLogado(inicio, fim);
            return ResponseEntity.ok(relatorio);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @GetMapping("/dashboard-stats")
    @Operation(summary = "Busca estatísticas (KPIs) para o dashboard do professor logado")
    public ResponseEntity<?> getDashboardStats() {
        try {
            ProfessorDashboardDTO stats = relatorioService.getDashboardProfessorLogado();
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}