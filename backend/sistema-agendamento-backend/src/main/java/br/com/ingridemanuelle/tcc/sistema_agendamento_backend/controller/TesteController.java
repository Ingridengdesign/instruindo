package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teste")
@Tag(name = "Teste", description = "Endpoint de teste")
@SecurityRequirement(name = "bearerAuth")
public class TesteController {

    @GetMapping
    @Operation(summary = "Endpoint de teste", description = "Retorna uma mensagem de sucesso se o usuário estiver autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<String> getHello() {

        return ResponseEntity.ok("Olá, usuário autenticado!");
    }
}