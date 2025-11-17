package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.controller;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agendamentos")
@Tag(name = "Agendamentos (Protegido)", description = "Endpoints para gerir agendamentos (ex: cancelar)")
@SecurityRequirement(name = "bearerAuth")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @PutMapping("/{id}/cancelar")
    @Operation(summary = "Cancela um agendamento (Aluno ou Professor)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento cancelado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    public ResponseEntity<?> cancelarAgendamento(@PathVariable Long id) {
        try {
            agendamentoService.cancelarAgendamento(id);
            return ResponseEntity.ok().body("Agendamento cancelado com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}