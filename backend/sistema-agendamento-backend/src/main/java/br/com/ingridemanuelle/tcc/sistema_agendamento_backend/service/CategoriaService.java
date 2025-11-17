package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.CategoriaDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(CategoriaDTO::new)
                .collect(Collectors.toList());
    }
}