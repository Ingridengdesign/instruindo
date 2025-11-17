package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.controller;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.ResetarSenhaDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.SolicitarResetSenhaDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/senha")
@Tag(name = "Senha", description = "Endpoints para recuperação de senha")
public class SenhaController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/solicitar-reset")
    @Operation(summary = "Solicita a redefinição de senha", description = "Inicia o processo de redefinição de senha para um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitação de redefinição de senha enviada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<String> solicitarReset(@Valid @RequestBody SolicitarResetSenhaDTO dto) {
        try {
            usuarioService.solicitarResetSenha(dto);

            return ResponseEntity.ok(
                    "Se o e-mail estiver registado, um link de recuperação será enviado.");
        } catch (Exception e) {

            return ResponseEntity.status(500).body("Erro ao processar a solicitação.");
        }
    }

    @PostMapping("/resetar")
    @Operation(summary = "Redefine a senha", description = "Redefine a senha de um usuário usando um token de redefinição")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    public ResponseEntity<String> resetarSenha(@Valid @RequestBody ResetarSenhaDTO dto) {
        try {
            usuarioService.resetarSenha(dto);
            return ResponseEntity.ok("Senha atualizada com sucesso.");
        } catch (RuntimeException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}