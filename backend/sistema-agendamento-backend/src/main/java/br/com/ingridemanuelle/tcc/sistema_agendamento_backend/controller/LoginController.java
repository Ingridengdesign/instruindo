package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.controller;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.LoginRequestDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.LoginResponseDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@Tag(name = "1. Autenticação (Público)", description = "Endpoints para Login (obter token JWT)")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    @Operation(summary = "Realizar login", description = "Autentica o utilizador com email e senha e retorna um Token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas (email ou senha incorretos)", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex: email ou senha em branco)", content = @Content)
    })
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {
        try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha());
            var authentication = authenticationManager.authenticate(authenticationToken);
            var userDetails = (UserDetails) authentication.getPrincipal();
            String tokenJwt = tokenService.gerarToken(userDetails);
            return ResponseEntity.ok(new LoginResponseDTO(tokenJwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).build();
        }
    }
}