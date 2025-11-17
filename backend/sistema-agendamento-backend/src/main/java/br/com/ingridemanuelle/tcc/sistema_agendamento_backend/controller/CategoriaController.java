package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.controller;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.CategoriaDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/categorias")
@Tag(name = "Categorias (Público)", description = "Endpoint público para listar todas as categorias de aulas")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    @Operation(summary = "Lista todas as categorias (disciplinas)", description = "Retorna um array com o ID e o Nome de todas as categorias ativas.")
    @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso")
    public ResponseEntity<List<CategoriaDTO>> getTodasCategorias() {
        List<CategoriaDTO> categorias = categoriaService.listarTodas();
        return ResponseEntity.ok(categorias);
    }
}