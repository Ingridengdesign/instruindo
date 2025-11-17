package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.AulaAgendadaDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Agendamento;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Aluno;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Avaliacao;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Professor;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Usuario;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.enums.TipoAvaliador;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.AgendamentoRepository;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.AlunoRepository;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.AvaliacaoRepository;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Transactional(readOnly = true)
    public List<AulaAgendadaDTO> buscarAulasAgendadasPorProfessorLogado() {
        Professor professor = professorRepository.findById(getUsuarioLogado().getIdUsuario())
                .orElseThrow(() -> new RuntimeException(
                        "Acesso negado: Somente professores podem ver agendamentos."));

        List<Agendamento> agendamentos = agendamentoRepository.findByAula_ProfessorAndStatus(professor,
                "CONFIRMADO");

        return agendamentos.stream()
                .map(agendamento -> {
                    Optional<Avaliacao> avaliacaoOpt = avaliacaoRepository
                            .findByAlunoAndAulaAndAvaliador(
                                    agendamento.getAluno(),
                                    agendamento.getAula(),
                                    TipoAvaliador.ALUNO);
                    return new AulaAgendadaDTO(agendamento, avaliacaoOpt.orElse(null));
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AulaAgendadaDTO> buscarHistoricoAlunoLogado() {

        Aluno aluno = alunoRepository.findById(getUsuarioLogado().getIdUsuario())
                .orElseThrow(() -> new RuntimeException(
                        "Acesso negado: Somente alunos podem ver seu histórico."));

        List<Agendamento> agendamentos = agendamentoRepository.findByAlunoOrderByAula_DataHoraDesc(aluno);

        return agendamentos.stream()
                .map(agendamento -> {

                    Optional<Avaliacao> avaliacaoOpt = avaliacaoRepository
                            .findByAlunoAndAulaAndAvaliador(
                                    agendamento.getAluno(),
                                    agendamento.getAula(),
                                    TipoAvaliador.ALUNO);

                    return new AulaAgendadaDTO(agendamento, avaliacaoOpt.orElse(null));
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AulaAgendadaDTO> buscarHistoricoProfessorLogado() {

        Professor professor = professorRepository.findById(getUsuarioLogado().getIdUsuario())
                .orElseThrow(() -> new RuntimeException(
                        "Acesso negado: Somente professores podem ver seu histórico."));

        List<Agendamento> agendamentos = agendamentoRepository
                .findByAula_ProfessorOrderByAula_DataHoraDesc(professor);

        return agendamentos.stream()
                .map(agendamento -> {

                    Optional<Avaliacao> avaliacaoOpt = avaliacaoRepository
                            .findByAlunoAndAulaAndAvaliador(
                                    agendamento.getAluno(),
                                    agendamento.getAula(),
                                    TipoAvaliador.ALUNO);

                    return new AulaAgendadaDTO(agendamento, avaliacaoOpt.orElse(null));
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelarAgendamento(Long idAgendamento) {

        Usuario usuarioLogado = getUsuarioLogado();
        Long idUsuarioLogado = usuarioLogado.getIdUsuario();

        Agendamento agendamento = agendamentoRepository.findById(idAgendamento)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado."));

        Professor professorDaAula = agendamento.getAula().getProfessor();
        Aluno alunoDaAula = agendamento.getAluno();

        boolean isDono = idUsuarioLogado.equals(professorDaAula.getIdUsuario()) ||
                idUsuarioLogado.equals(alunoDaAula.getIdUsuario());

        if (!isDono) {
            throw new RuntimeException("Acesso negado: Você não faz parte deste agendamento.");
        }

        if (!"CONFIRMADO".equalsIgnoreCase(agendamento.getStatus())) {
            throw new RuntimeException("Apenas agendamentos 'CONFIRMADOS' podem ser cancelados.");
        }

        LocalDateTime dataHoraAula = agendamento.getAula().getDataHora();
        if (dataHoraAula.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Não é possível cancelar uma aula que já ocorreu.");
        }

        agendamento.setStatus("CANCELADO");
        agendamentoRepository.save(agendamento);

        if (idUsuarioLogado.equals(alunoDaAula.getIdUsuario())) {
            String msg = String.format("O aluno %s cancelou a aula de %s do dia %s.",
                    alunoDaAula.getNome(), agendamento.getAula().getTitulo(),
                    dataHoraAula.toLocalDate());
            notificacaoService.criarNotificacao(professorDaAula, msg, "AGENDAMENTO_CANCELADO",
                    "/minha-agenda");
        } else {
            String msg = String.format("O professor %s cancelou sua aula de %s do dia %s.",
                    professorDaAula.getNome(), agendamento.getAula().getTitulo(),
                    dataHoraAula.toLocalDate());
            notificacaoService.criarNotificacao(alunoDaAula, msg, "AGENDAMENTO_CANCELADO",
                    "/meus-agendamentos");
        }
    }

    private Usuario getUsuarioLogado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}