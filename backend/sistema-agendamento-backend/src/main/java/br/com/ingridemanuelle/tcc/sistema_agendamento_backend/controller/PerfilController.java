package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.controller;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.UpdateAlunoDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.UpdateProfessorDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil")
@Tag(name = "Perfil", description = "Endpoints para gerenciamento de perfil de usuário")
public class PerfilController {

    @Autowired
    private UsuarioService usuarioService;

    @PutMapping("/aluno")
    @Operation(summary = "Atualiza o perfil do aluno", description = "Atualiza as informações do perfil do aluno autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil de aluno atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> atualizarPerfilAluno(@Valid @RequestBody UpdateAlunoDTO dto) {
        try {
            usuarioService.atualizarPerfilAluno(dto);
            return ResponseEntity.ok().body("Perfil de aluno atualizado com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/professor")
    @Operation(summary = "Atualiza o perfil do professor", description = "Atualiza as informações do perfil do professor autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil de professor atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> atualizarPerfilProfessor(@Valid @RequestBody UpdateProfessorDTO dto) {
        try {
            usuarioService.atualizarPerfilProfessor(dto);
            return ResponseEntity.ok().body("Perfil de professor atualizado com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Busca o perfil do usuário logado", description = "Retorna o perfil do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getMeuPerfil() {
        try {
            Object perfilDto = usuarioService.buscarPerfilUsuarioLogado();
            return ResponseEntity.ok(perfilDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping
    @Operation(summary = "Desativa o perfil do usuário", description = "Desativa o perfil do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta desativada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> deletarMeuPerfil() {
        try {
            usuarioService.desativarMeuPerfil();
            return ResponseEntity.ok().body("Conta desativada com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/foto")
    @Operation(summary = "Remove a foto de perfil do utilizador logado")
    public ResponseEntity<?> removerFotoPerfil() {
        try {
            usuarioService.removerFotoPerfil();
            return ResponseEntity.ok().body("Foto de perfil removida com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}