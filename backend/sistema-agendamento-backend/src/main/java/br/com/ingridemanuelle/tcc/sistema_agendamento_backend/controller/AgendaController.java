package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.controller;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.AulaAgendadaDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.BloqueioHorarioInputDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.BloqueioHorarioOutputDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.DisponibilidadeInputDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.DisponibilidadeOutputDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service.AgendamentoService;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service.DisponibilidadeService;
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
@RequestMapping("/api/agenda/professor")
@Tag(name = "Agenda (Professor)", description = "Endpoints para o Professor gerir a sua agenda")
@SecurityRequirement(name = "bearerAuth")
public class AgendaController {

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private DisponibilidadeService disponibilidadeService;

    @GetMapping("/aulas")
    @Operation(summary = "Busca as aulas agendadas do professor logado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aulas encontradas com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<?> getMinhasAulasAgendadas() {
        try {
            List<AulaAgendadaDTO> aulas = agendamentoService.buscarAulasAgendadasPorProfessorLogado();
            return ResponseEntity.ok(aulas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @GetMapping("/disponibilidade")
    @Operation(summary = "Busca a disponibilidade do professor logado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disponibilidade encontrada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<?> getMinhaDisponibilidade() {
        try {
            List<DisponibilidadeOutputDTO> disponibilidades = disponibilidadeService.buscarDisponibilidade();
            return ResponseEntity.ok(disponibilidades);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PutMapping("/disponibilidade")
    @Operation(summary = "Atualiza a disponibilidade do professor logado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disponibilidade atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    public ResponseEntity<?> atualizarMinhaDisponibilidade(
            @Valid @RequestBody List<DisponibilidadeInputDTO> novaLista) {
        try {
            disponibilidadeService.atualizarDisponibilidade(novaLista);
            return ResponseEntity.ok().body("Disponibilidade atualizada com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/bloqueios")
    @Operation(summary = "Professor vê seus bloqueios de horário futuros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bloqueios encontrados com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<?> getMeusBloqueios() {
        try {
            List<BloqueioHorarioOutputDTO> bloqueios = disponibilidadeService.listarMeusBloqueios();
            return ResponseEntity.ok(bloqueios);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PostMapping("/bloqueios")
    @Operation(summary = "Professor cria um novo bloqueio de horário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bloqueio criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    public ResponseEntity<?> criarBloqueio(@Valid @RequestBody BloqueioHorarioInputDTO dto) {
        try {
            BloqueioHorarioOutputDTO bloqueioCriado = disponibilidadeService.criarBloqueio(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(bloqueioCriado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/bloqueios/{id}")
    @Operation(summary = "Professor apaga um bloqueio de horário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bloqueio apagado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    public ResponseEntity<?> apagarBloqueio(@PathVariable Long id) {
        try {
            disponibilidadeService.apagarBloqueio(id);
            return ResponseEntity.ok().body("Bloqueio apagado com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}