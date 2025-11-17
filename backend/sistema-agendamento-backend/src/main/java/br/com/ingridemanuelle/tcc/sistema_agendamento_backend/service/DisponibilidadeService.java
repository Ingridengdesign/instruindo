package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.BloqueioHorarioInputDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.BloqueioHorarioOutputDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.DisponibilidadeInputDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.DisponibilidadeOutputDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Agendamento;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.BloqueioHorario;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Disponibilidade;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Professor;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Usuario;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.AgendamentoRepository;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.BloqueioHorarioRepository;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.DisponibilidadeRepository;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.ProfessorRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DisponibilidadeService {

    @Autowired
    private DisponibilidadeRepository disponibilidadeRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private BloqueioHorarioRepository bloqueioHorarioRepository;
    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Transactional
    public void atualizarDisponibilidade(List<DisponibilidadeInputDTO> novaListaDto) {
        Professor professor = getProfessorLogado();

        List<Disponibilidade> novaListaEntidades = new ArrayList<>();
        for (DisponibilidadeInputDTO dto : novaListaDto) {
            if (dto.getHoraInicio() == null || dto.getHoraFim() == null || dto.getDiaDaSemana() == null) {
                throw new RuntimeException(
                        "Dados de disponibilidade inválidos: Dia, hora início e hora fim são obrigatórios.");
            }
            if (dto.getHoraFim().isBefore(dto.getHoraInicio()) || dto.getHoraFim().equals(dto.getHoraInicio())) {
                throw new RuntimeException(
                        "Dados de disponibilidade inválidos: Hora de fim deve ser após a hora de início.");
            }
            novaListaEntidades.add(
                    new Disponibilidade(professor, dto.getDiaDaSemana(), dto.getHoraInicio(), dto.getHoraFim()));
        }
        disponibilidadeRepository.deleteByProfessor(professor);
        disponibilidadeRepository.saveAll(novaListaEntidades);
    }

    @Transactional(readOnly = true)
    public List<DisponibilidadeOutputDTO> buscarDisponibilidade() {
        Professor professor = getProfessorLogado();
        List<Disponibilidade> disponibilidades = disponibilidadeRepository.findByProfessor(professor);
        return disponibilidades.stream()
                .map(DisponibilidadeOutputDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BloqueioHorarioOutputDTO> listarMeusBloqueios() {
        Professor professor = getProfessorLogado();
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime umAnoDepois = agora.plusYears(1);

        List<BloqueioHorario> bloqueios = bloqueioHorarioRepository.findBloqueiosNoIntervalo(professor, agora,
                umAnoDepois);

        return bloqueios.stream().map(BloqueioHorarioOutputDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public BloqueioHorarioOutputDTO criarBloqueio(BloqueioHorarioInputDTO dto) {
        Professor professor = getProfessorLogado();

        if (dto.getDataHoraFim().isBefore(dto.getDataHoraInicio())
                || dto.getDataHoraFim().equals(dto.getDataHoraInicio())) {
            throw new RuntimeException("Horário inválido: A data/hora de fim deve ser após a data/hora de início.");
        }

        boolean haConflito = agendamentoRepository.existsConflitoDeHorarioEmIntervalo(
                professor,
                dto.getDataHoraInicio(),
                dto.getDataHoraFim());

        if (haConflito) {
            throw new RuntimeException("Conflito: Você já possui uma aula CONFIRMADA neste intervalo de horário.");
        }

        BloqueioHorario bloqueio = new BloqueioHorario(
                professor, dto.getDataHoraInicio(), dto.getDataHoraFim(), dto.getMotivo());
        bloqueioHorarioRepository.save(bloqueio);
        return new BloqueioHorarioOutputDTO(bloqueio);
    }

    @Transactional
    public void apagarBloqueio(Long idBloqueio) {
        Professor professor = getProfessorLogado();
        BloqueioHorario bloqueio = bloqueioHorarioRepository
                .findById(idBloqueio)
                .orElseThrow(() -> new RuntimeException("Bloqueio não encontrado."));

        if (!bloqueio.getProfessor().getIdUsuario().equals(professor.getIdUsuario())) {
            throw new RuntimeException("Acesso negado: Você não é o dono deste bloqueio.");
        }
        bloqueioHorarioRepository.delete(bloqueio);
    }

    @Transactional(readOnly = true)
    public List<String> getHorariosDisponiveis(Long idProfessor, LocalDate data) {

        Professor professor = professorRepository.findByIdUsuarioAndAtivoTrue(idProfessor)
                .orElseThrow(() -> new RuntimeException("Professor ativo não encontrado."));

        DayOfWeek diaDaSemana = data.getDayOfWeek();
        Optional<Disponibilidade> recorrenteOpt = disponibilidadeRepository.findByProfessorAndDiaDaSemana(professor,
                diaDaSemana);

        if (recorrenteOpt.isEmpty()) {
            return new ArrayList<>();
        }

        Disponibilidade recorrente = recorrenteOpt.get();
        LocalTime horaInicio = recorrente.getHoraInicio();
        LocalTime horaFim = recorrente.getHoraFim();

        Set<LocalTime> slotsPossiveis = new HashSet<>();
        LocalTime slotAtual = horaInicio;
        while (slotAtual.isBefore(horaFim)) {
            slotsPossiveis.add(slotAtual);
            slotAtual = slotAtual.plusHours(1);
        }

        LocalDateTime inicioDoDia = data.atStartOfDay();
        LocalDateTime fimDoDia = data.plusDays(1).atStartOfDay();

        Set<LocalTime> slotsOcupados = new HashSet<>();

        List<Agendamento> agendamentos = agendamentoRepository.findAgendamentosConfirmadosEntre(
                professor, inicioDoDia, fimDoDia);
        for (Agendamento ag : agendamentos) {
            LocalTime inicioOcupado = ag.getAula().getDataHora().toLocalTime();
            LocalTime fimOcupado = ag.getAula().getDataHoraFim().toLocalTime();

            LocalTime slotOcupadoAtual = inicioOcupado.truncatedTo(ChronoUnit.HOURS);
            while (slotOcupadoAtual.isBefore(fimOcupado)) {
                slotsOcupados.add(slotOcupadoAtual);
                slotOcupadoAtual = slotOcupadoAtual.plusHours(1);
            }
        }

        List<BloqueioHorario> bloqueios = bloqueioHorarioRepository.findBloqueiosNoIntervalo(
                professor, inicioDoDia, fimDoDia);
        for (BloqueioHorario bloqueio : bloqueios) {
            LocalTime inicioOcupado = bloqueio.getDataHoraInicio().toLocalTime();
            LocalTime fimOcupado = bloqueio.getDataHoraFim().toLocalTime();

            LocalTime slotOcupadoAtual = inicioOcupado.truncatedTo(ChronoUnit.HOURS);
            while (slotOcupadoAtual.isBefore(fimOcupado)) {
                slotsOcupados.add(slotOcupadoAtual);
                slotOcupadoAtual = slotOcupadoAtual.plusHours(1);
            }
        }

        slotsPossiveis.removeAll(slotsOcupados);

        return slotsPossiveis.stream()
                .map(LocalTime::toString)
                .sorted()
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> getDiasDisponiveisNoMes(Long idProfessor, int ano, int mes) {

        professorRepository.findByIdUsuarioAndAtivoTrue(idProfessor)
                .orElseThrow(() -> new RuntimeException("Professor ativo não encontrado."));

        YearMonth mesAno = YearMonth.of(ano, mes);
        LocalDate diaInicio = mesAno.atDay(1);
        LocalDate diaFim = mesAno.atEndOfMonth();

        List<String> diasDisponiveis = new ArrayList<>();

        for (LocalDate dia = diaInicio; !dia.isAfter(diaFim); dia = dia.plusDays(1)) {

            if (dia.isBefore(LocalDate.now())) {
                continue;
            }

            List<String> horariosDoDia = this.getHorariosDisponiveis(idProfessor, dia);

            if (!horariosDoDia.isEmpty()) {
                diasDisponiveis.add(dia.toString());
            }
        }
        return diasDisponiveis;
    }

    private Professor getProfessorLogado() {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return professorRepository
                .findById(usuarioLogado.getIdUsuario())
                .orElseThrow(
                        () -> new RuntimeException("Acesso negado: Somente professores podem executar esta ação."));
    }
}