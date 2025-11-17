package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.ProfessorCardDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.ProfessorPerfilDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Professor;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.ProfessorRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Transactional(readOnly = true)
    public List<ProfessorCardDTO> buscarProfessores(
            Long idCategoria, Double precoMax, Double notaMin) {

        List<Professor> professores = professorRepository.searchProfessores(idCategoria, precoMax, notaMin);

        return professores.stream()
                .map(professor -> new ProfessorCardDTO(professor))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProfessorPerfilDTO buscarPerfilProfessor(Long idProfessor) {

        Professor professor = professorRepository
                .findByIdUsuarioAndAtivoTrue(idProfessor)
                .orElseThrow(
                        () -> new RuntimeException("Professor ativo não encontrado com o ID: " + idProfessor));

        return new ProfessorPerfilDTO(professor);
    }
}