package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.controller;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.CadastroAlunoDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.CadastroProfessorDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cadastro")
@Tag(name = "Cadastro", description = "Endpoints para cadastro de novos usuários")
public class CadastroController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/aluno")
    @Operation(summary = "Cadastra um novo aluno", description = "Cria um novo usuário do tipo Aluno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Aluno cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    public ResponseEntity<String> cadastrarAluno(@Valid @RequestBody CadastroAlunoDTO dto) {
        try {
            usuarioService.cadastrarAluno(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Aluno cadastrado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/professor")
    @Operation(summary = "Cadastra um novo professor", description = "Cria um novo usuário do tipo Professor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Professor cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    public ResponseEntity<String> cadastrarProfessor(@Valid @RequestBody CadastroProfessorDTO dto) {
        try {
            usuarioService.cadastrarProfessor(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Professor cadastrado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}