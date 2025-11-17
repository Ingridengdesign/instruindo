package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.controller;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Usuario;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service.FileStorageService;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@Tag(name = "Ficheiros (Upload/Download)", description = "Endpoints para upload e visualização de ficheiros")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/api/perfil/upload-foto")
    @Operation(summary = "Upload da foto de perfil", description = "Utilizador logado envia a sua foto.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> uploadFotoPerfil(@RequestParam("file") MultipartFile file) {
        try {

            Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long idUsuario = usuarioLogado.getIdUsuario();
            String nomeBase = "user-" + idUsuario;

            String nomeFicheiro = fileStorageService.storeFile(file, nomeBase);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/")
                    .path(nomeFicheiro)
                    .toUriString();

            usuarioService.atualizarFotoUrl(idUsuario, fileDownloadUri);

            return ResponseEntity.ok().body("{\"fotoUrl\": \"" + fileDownloadUri + "\"}");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Não foi possível fazer o upload do ficheiro. " + e.getMessage());
        }
    }

    @GetMapping("/api/files/{nomeFicheiro:.+}")
    @Operation(summary = "Download/Visualização de ficheiro", description = "Endpoint público para carregar imagens.")
    public ResponseEntity<Resource> downloadFile(@PathVariable String nomeFicheiro, HttpServletRequest request) {

        Resource resource = fileStorageService.loadFileAsResource(nomeFicheiro);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {

        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}