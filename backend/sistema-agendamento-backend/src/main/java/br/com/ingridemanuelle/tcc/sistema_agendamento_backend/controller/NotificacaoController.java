package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.controller;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.NotificacaoDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Notificacao;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service.NotificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notificacoes")
@Tag(name = "Notificações", description = "Endpoints para gerenciamento de notificações")
public class NotificacaoController {

    @Autowired
    private NotificacaoService notificacaoService;

    @GetMapping
    @Operation(summary = "Busca as notificações do usuário logado", description = "Retorna uma lista de notificações para o usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificações encontradas com sucesso")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<NotificacaoDTO>> getMinhasNotificacoes(
            @RequestParam(defaultValue = "false") boolean apenasNaoLidas) {

        List<Notificacao> notificacoes = notificacaoService.buscarMinhasNotificacoes(apenasNaoLidas);

        List<NotificacaoDTO> dtos = notificacoes.stream().map(NotificacaoDTO::new).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/contagem-nao-lidas")
    @Operation(summary = "Conta as notificações não lidas", description = "Retorna o número de notificações não lidas para o usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contagem retornada com sucesso")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Long> getContagemNaoLidas() {
        long contagem = notificacaoService.contarMinhasNaoLidas();
        return ResponseEntity.ok(contagem);
    }

    @PutMapping("/{id}/marcar-lida")
    @Operation(summary = "Marca uma notificação como lida", description = "Marca uma notificação específica como lida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificação marcada como lida"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> marcarComoLida(@PathVariable Long id) {
        try {
            notificacaoService.marcarComoLida(id);
            return ResponseEntity.ok().body("Notificação marcada como lida.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PostMapping("/marcar-todas-lidas")
    @Operation(summary = "Marca todas as notificações como lidas", description = "Marca todas as notificações do usuário autenticado como lidas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificações marcadas como lidas")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> marcarTodasComoLidas() {
        int count = notificacaoService.marcarTodasMinhasComoLidas();
        return ResponseEntity.ok().body(count + " notificações marcadas como lidas.");
    }
}