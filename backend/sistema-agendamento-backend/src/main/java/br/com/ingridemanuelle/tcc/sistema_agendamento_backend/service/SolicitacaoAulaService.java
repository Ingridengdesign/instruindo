package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.AcaoSolicitacaoDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.SolicitacaoRequestDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.SolicitacaoResponseDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.*;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SolicitacaoAulaService {

    @Autowired
    private SolicitacaoAulaRepository solicitacaoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private AulaRepository aulaRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void criarSolicitacao(SolicitacaoRequestDTO dto) {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Aluno aluno = alunoRepository.findById(usuarioLogado.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Erro: Aluno não encontrado."));
        Professor professor = professorRepository.findById(dto.getIdProfessor())
                .orElseThrow(() -> new RuntimeException("Erro: Professor não encontrado."));

        if (!professor.isAtivo()) {
            throw new RuntimeException("Erro: Este professor não está mais ativo na plataforma.");
        }
        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Erro: Categoria não encontrada."));

        SolicitacaoAula novaSolicitacao = new SolicitacaoAula();
        novaSolicitacao.setAluno(aluno);
        novaSolicitacao.setProfessor(professor);
        novaSolicitacao.setCategoria(categoria);
        novaSolicitacao.setDataSolicitacao(dto.getDataSolicitacao());
        novaSolicitacao.setDetalhes(dto.getDetalhes());
        novaSolicitacao.setStatus("PENDENTE");

        solicitacaoRepository.save(novaSolicitacao);

        String mensagem = String.format("Nova solicitação de aula recebida do aluno %s para %s.",
                aluno.getNome(),
                novaSolicitacao.getCategoria().getTitulo());
        String link = "/minhas-solicitacoes";
        notificacaoService.criarNotificacao(professor, mensagem, "NOVA_SOLICITACAO", link);

        String assuntoEmail = "Você recebeu uma nova solicitação de aula!";
        String corpoEmail = String.format(
                "Olá, %s.\n\nO aluno %s solicitou uma aula de %s para a data %s.\n\n" +
                        "Acesse a plataforma para aceitar ou recusar.\n\nDetalhes: %s",
                professor.getNome(),
                aluno.getNome(),
                categoria.getTitulo(),
                novaSolicitacao.getDataSolicitacao().toString(),
                novaSolicitacao.getDetalhes());
        emailService.enviarEmailSimples(professor.getEmail(), assuntoEmail, corpoEmail);
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoResponseDTO> buscarSolicitacoesPorProfessor() {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long usuarioId = usuarioLogado.getIdUsuario();
        Professor professor = professorRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException(
                        "Acesso negado: Somente professores podem ver solicitações."));
        List<SolicitacaoAula> solicitacoes = solicitacaoRepository.findByProfessor(professor);
        return solicitacoes.stream()
                .map(SolicitacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void responderSolicitacao(Long idSolicitacao, AcaoSolicitacaoDTO dto) {

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SolicitacaoAula solicitacao = solicitacaoRepository.findById(idSolicitacao)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada."));

        Professor professorDaSolicitacao = solicitacao.getProfessor();
        Aluno alunoDaSolicitacao = solicitacao.getAluno();
        Categoria categoriaDaSolicitacao = solicitacao.getCategoria();

        if (!professorDaSolicitacao.getIdUsuario().equals(usuarioLogado.getIdUsuario())) {
            throw new RuntimeException("Acesso negado: Você não é o professor desta solicitação.");
        }
        if (!"PENDENTE".equalsIgnoreCase(solicitacao.getStatus())) {
            throw new RuntimeException("Esta solicitação já foi respondida.");
        }

        String statusFinal;
        String tipoNotificacao;
        String mensagemNotificacao;
        String linkParaAluno = "/meus-agendamentos";

        if ("ACEITAR".equalsIgnoreCase(dto.getAcao())) {

            LocalDateTime inicioNovaAula = solicitacao.getDataSolicitacao();
            LocalDateTime fimNovaAula = inicioNovaAula.plusHours(1);

            boolean haConflito = agendamentoRepository.existsConflitoDeHorarioEmIntervalo(
                    professorDaSolicitacao,
                    inicioNovaAula,
                    fimNovaAula);

            if (haConflito) {
                throw new RuntimeException(
                        "Conflito de horário! Você já possui uma aula confirmada que se sobrepõe a este horário.");
            }

            solicitacao.setStatus("ACEITA");
            statusFinal = "ACEITA";
            tipoNotificacao = "SOLICITACAO_ACEITA";

            Aula novaAula = new Aula();
            novaAula.setProfessor(professorDaSolicitacao);
            novaAula.setCategoria(categoriaDaSolicitacao);
            novaAula.setDataHora(inicioNovaAula);
            novaAula.setDataHoraFim(fimNovaAula);
            novaAula.setPreco(professorDaSolicitacao.getPrecoPorHora());
            novaAula.setTitulo("Aula de " + categoriaDaSolicitacao.getTitulo());
            novaAula.setDescricao(solicitacao.getDetalhes());
            novaAula.setLocal(dto.getLocal());
            aulaRepository.save(novaAula);

            Agendamento novoAgendamento = new Agendamento();
            novoAgendamento.setAluno(alunoDaSolicitacao);
            novoAgendamento.setAula(novaAula);
            novoAgendamento.setSolicitacao(solicitacao);
            novoAgendamento.setDataAgendamento(LocalDateTime.now());
            novoAgendamento.setStatus("CONFIRMADO");
            agendamentoRepository.save(novoAgendamento);

            mensagemNotificacao = String.format("Sua solicitação de aula de %s com %s foi ACEITA.",
                    categoriaDaSolicitacao.getTitulo(),
                    professorDaSolicitacao.getNome());

            String assuntoEmail = "Sua solicitação de aula foi Aceita!";
            String corpoEmail = String.format(
                    "Olá, %s.\n\nBoas notícias! O professor %s aceitou a sua solicitação de aula de %s.\n\n"
                            +
                            "Data/Hora: %s\n" +
                            "Local/Link: %s\n\n" +
                            "Acesse a plataforma para ver os detalhes.",
                    alunoDaSolicitacao.getNome(),
                    professorDaSolicitacao.getNome(),
                    categoriaDaSolicitacao.getTitulo(),
                    solicitacao.getDataSolicitacao().toString(),
                    (dto.getLocal() != null ? dto.getLocal() : "A ser definido pelo professor"));

            emailService.enviarEmailSimples(alunoDaSolicitacao.getEmail(), assuntoEmail, corpoEmail);

        } else if ("RECUSAR".equalsIgnoreCase(dto.getAcao())) {
            solicitacao.setStatus("RECUSADA");
            statusFinal = "RECUSADA";
            tipoNotificacao = "SOLICITACAO_RECUSADA";
            mensagemNotificacao = String.format("Sua solicitação de aula de %s com %s foi RECUSADA.",
                    categoriaDaSolicitacao.getTitulo(),
                    professorDaSolicitacao.getNome());

            String assuntoEmail = "Sua solicitação de aula foi Recusada";
            emailService.enviarEmailSimples(alunoDaSolicitacao.getEmail(), assuntoEmail,
                    mensagemNotificacao);

        } else {
            throw new RuntimeException("Ação inválida. Use 'ACEITAR' ou 'RECUSAR'.");
        }

        solicitacaoRepository.save(solicitacao);

        notificacaoService.criarNotificacao(alunoDaSolicitacao, mensagemNotificacao, tipoNotificacao,
                linkParaAluno);
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoResponseDTO> buscarMinhasSolicitacoes() {

        Aluno aluno = alunoRepository.findById(getUsuarioLogado().getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Acesso negado: Somente alunos podem ver solicitações."));

        List<SolicitacaoAula> solicitacoes = solicitacaoRepository.findByAlunoOrderByDataSolicitacaoDesc(aluno);

        return solicitacoes.stream()
                .map(SolicitacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    private Usuario getUsuarioLogado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
