package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.AvaliacaoRequestDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.ProfessorAvaliacaoAlunoDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.*;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.enums.TipoAvaliador;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;
    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private AulaRepository aulaRepository;
    @Autowired
    private AgendamentoRepository agendamentoRepository;
    @Autowired
    private ProfessorRepository professorRepository;

    @Transactional
    public void criarAvaliacao(Long idAula, AvaliacaoRequestDTO dto) {

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Aluno aluno = alunoRepository
                .findById(usuarioLogado.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Acesso negado: Somente alunos podem avaliar."));

        Aula aula = aulaRepository.findById(idAula).orElseThrow(() -> new RuntimeException("Aula não encontrada."));

        boolean participou = agendamentoRepository.existsByAlunoAndAulaAndStatusIn(
                aluno, aula, List.of("CONFIRMADO", "REALIZADO"));
        if (!participou) {
            throw new RuntimeException("Acesso negado: Você não participou desta aula.");
        }

        boolean jaAvaliou = avaliacaoRepository.existsByAlunoAndAulaAndAvaliador(aluno, aula, TipoAvaliador.ALUNO);
        if (jaAvaliou) {
            throw new RuntimeException("Você já avaliou esta aula.");
        }

        Avaliacao novaAvaliacao = new Avaliacao();
        novaAvaliacao.setAluno(aluno);
        novaAvaliacao.setProfessor(aula.getProfessor());
        novaAvaliacao.setAula(aula);
        novaAvaliacao.setNota(dto.getNota());
        novaAvaliacao.setComentario(dto.getComentario());
        novaAvaliacao.setAvaliador(TipoAvaliador.ALUNO);
        avaliacaoRepository.save(novaAvaliacao);
    }

    @Transactional
    public void criarAvaliacaoProfessorParaAluno(Long idAula, ProfessorAvaliacaoAlunoDTO dto) {

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Professor professor = professorRepository
                .findById(usuarioLogado.getIdUsuario())
                .orElseThrow(
                        () -> new RuntimeException("Acesso negado: Somente professores podem avaliar alunos."));

        Aula aula = aulaRepository.findById(idAula).orElseThrow(() -> new RuntimeException("Aula não encontrada."));

        if (!aula.getProfessor().getIdUsuario().equals(professor.getIdUsuario())) {
            throw new RuntimeException("Acesso negado: Você não é o professor desta aula.");
        }

        Agendamento agendamento = agendamentoRepository
                .findByAula(aula)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado para esta aula."));
        Aluno aluno = agendamento.getAluno();

        boolean jaAvaliou = avaliacaoRepository.existsByProfessorAndAulaAndAvaliador(
                professor, aula, TipoAvaliador.PROFESSOR);
        if (jaAvaliou) {
            throw new RuntimeException("Você já avaliou este aluno para esta aula.");
        }

        Avaliacao novaAvaliacao = new Avaliacao();
        novaAvaliacao.setProfessor(professor);
        novaAvaliacao.setAluno(aluno);
        novaAvaliacao.setAula(aula);
        novaAvaliacao.setNota(dto.getNota());
        novaAvaliacao.setComentario(dto.getComentario());
        novaAvaliacao.setAvaliador(TipoAvaliador.PROFESSOR);
        avaliacaoRepository.save(novaAvaliacao);
    }
}