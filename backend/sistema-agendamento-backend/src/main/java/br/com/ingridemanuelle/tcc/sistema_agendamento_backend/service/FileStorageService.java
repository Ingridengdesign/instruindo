package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public FileStorageService() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Não foi possível criar o diretório para guardar os uploads.", ex);
        }
    }

    public String storeFile(MultipartFile file, String nomeBase) {
        try {

            String extensao = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String nomeFicheiro = nomeBase + extensao;

            Path targetLocation = this.fileStorageLocation.resolve(nomeFicheiro);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return nomeFicheiro;

        } catch (IOException ex) {
            throw new RuntimeException("Não foi possível guardar o ficheiro " + nomeBase, ex);
        }
    }

    public Resource loadFileAsResource(String nomeFicheiro) {
        try {
            Path filePath = this.fileStorageLocation.resolve(nomeFicheiro).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("Ficheiro não encontrado: " + nomeFicheiro);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Ficheiro não encontrado: " + nomeFicheiro, ex);
        }
    }
}