package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.ProfessorDashboardDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.RelatorioProfessorDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Professor;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Usuario;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.enums.TipoAvaliador;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.AgendamentoRepository;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.AvaliacaoRepository;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.ProfessorRepository;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.SolicitacaoAulaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class RelatorioService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private SolicitacaoAulaRepository solicitacaoAulaRepository;

    @Transactional(readOnly = true)
    public RelatorioProfessorDTO gerarRelatorioProfessorLogado(LocalDate dataInicio, LocalDate dataFim) {

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Professor professor = professorRepository.findById(usuarioLogado.getIdUsuario())
                .orElseThrow(() -> new RuntimeException(
                        "Acesso negado: Somente professores podem gerar relatórios."));

        LocalDateTime inicioPeriodo = dataInicio.atStartOfDay();
        LocalDateTime fimPeriodo = dataFim.atTime(LocalTime.MAX);

        Long totalAulas = agendamentoRepository.countAgendamentosConfirmadosPeriodo(
                professor, inicioPeriodo, fimPeriodo);

        Double faturamento = agendamentoRepository.sumFaturamentoConfirmadoPeriodo(
                professor, inicioPeriodo, fimPeriodo);

        return new RelatorioProfessorDTO(dataInicio, dataFim, totalAulas, faturamento);
    }

    @Transactional(readOnly = true)
    public ProfessorDashboardDTO getDashboardProfessorLogado() {

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Professor professor = professorRepository.findById(usuarioLogado.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Acesso negado: Somente professores podem ver o dashboard."));

        long totalSolicitadas = agendamentoRepository.countByAula_Professor(professor);

        long totalConcluidas = agendamentoRepository.countByAula_ProfessorAndStatus(professor, "REALIZADO");

        long totalAvaliacoes = avaliacaoRepository.countByProfessorAndAvaliador(professor, TipoAvaliador.ALUNO);

        long totalNovosPedidos = solicitacaoAulaRepository.countByProfessorAndStatus(professor, "PENDENTE");

        return new ProfessorDashboardDTO(totalSolicitadas, totalConcluidas, totalAvaliacoes, totalNovosPedidos);
    }
}