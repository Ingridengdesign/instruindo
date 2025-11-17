package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.controller;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.MensagemDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.NovaMensagemDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service.MensagemService;
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
@RequestMapping("/api/mensagens")
@Tag(name = "Mensagens", description = "Endpoints para troca de mensagens")
public class MensagemController {

    @Autowired
    private MensagemService mensagemService;

    @PostMapping
    @Operation(summary = "Envia uma nova mensagem", description = "Envia uma nova mensagem para outro usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mensagem enviada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> enviarMensagem(@Valid @RequestBody NovaMensagemDTO dto) {
        try {
            MensagemDTO mensagemCriada = mensagemService.enviarMensagem(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(mensagemCriada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/conversa/{idOutroUsuario}")
    @Operation(summary = "Busca o histórico de conversa", description = "Busca o histórico de mensagens entre o usuário logado e outro usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico de conversa encontrado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getHistoricoConversa(@PathVariable Long idOutroUsuario) {
        try {
            List<MensagemDTO> historico = mensagemService.buscarHistoricoConversa(idOutroUsuario);
            return ResponseEntity.ok(historico);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/conversa/{idOutroUsuario}/marcar-como-lida")
    @Operation(summary = "Marca a conversa como lida", description = "Marca todas as mensagens de uma conversa como lidas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mensagens marcadas como lidas"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> marcarConversaComoLida(@PathVariable Long idOutroUsuario) {
        try {
            mensagemService.marcarConversaComoLida(idOutroUsuario);
            return ResponseEntity.ok().body("Mensagens marcadas como lidas.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}