package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.config;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.*;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.enums.TipoAvaliador;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

        @Autowired
        private CategoriaRepository categoriaRepository;
        @Autowired
        private ProfessorRepository professorRepository;
        @Autowired
        private AlunoRepository alunoRepository;
        @Autowired
        private AulaRepository aulaRepository;
        @Autowired
        private AgendamentoRepository agendamentoRepository;
        @Autowired
        private AvaliacaoRepository avaliacaoRepository;
        @Autowired
        private DisponibilidadeRepository disponibilidadeRepository;
        @Autowired
        private BloqueioHorarioRepository bloqueioHorarioRepository;
        @Autowired
        private SolicitacaoAulaRepository solicitacaoAulaRepository;
        @Autowired
        private MensagemRepository mensagemRepository;
        @Autowired
        private NotificacaoRepository notificacaoRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        public void run(String... args) throws Exception {

                if (categoriaRepository.count() > 0) {
                        System.out.println("--- [DATA SEEDER] Banco de dados já populado. Ignorando. ---");
                        return;
                }

                System.out.println("--- [INICIANDO DATA SEEDER v5 - CORRIGIDO] ---");

                LocalDateTime agora = LocalDateTime.now();
                LocalDate hoje = LocalDate.now();

                Categoria catMat = new Categoria();
                catMat.setTitulo("Matemática");
                catMat.setDescricao("Aulas de cálculo, álgebra e geometria.");
                catMat = categoriaRepository.save(catMat);

                Categoria catProg = new Categoria();
                catProg.setTitulo("Programação");
                catProg.setDescricao("Aulas de Java, Python e Web.");
                catProg = categoriaRepository.save(catProg);

                Categoria catFis = new Categoria();
                catFis.setTitulo("Física");
                catFis.setDescricao("Aulas de mecânica clássica e eletromagnetismo.");
                catFis = categoriaRepository.save(catFis);

                Categoria catHist = new Categoria();
                catHist.setTitulo("História");
                catHist.setDescricao("História antiga, medieval e moderna.");
                catHist = categoriaRepository.save(catHist);

                Categoria catQui = new Categoria();
                catQui.setTitulo("Química");
                catQui.setDescricao("Química orgânica e inorgânica.");
                catQui = categoriaRepository.save(catQui);

                List<Categoria> categorias = List.of(catMat, catProg, catFis, catHist, catQui);

                Professor profA = new Professor();
                profA.setNome("Prof. Alan Turing");
                profA.setEmail("alan@turing.com");
                profA.setSenha(passwordEncoder.encode("senha123"));
                profA.setDataCadastro(hoje);
                profA.setPrecoPorHora(120.00);
                profA.setCategorias(Set.of(catMat, catProg));
                profA.setFotoUrl("https://exemplo.com/fotos/alan.png");
                profA = professorRepository.save(profA);

                Professor profB = new Professor();
                profB.setNome("Prof. Ada Lovelace");
                profB.setEmail("ada@lovelace.com");
                profB.setSenha(passwordEncoder.encode("senha123"));
                profB.setDataCadastro(hoje);
                profB.setPrecoPorHora(90.00);
                profB.setCategorias(Set.of(catProg, catFis));
                profB = professorRepository.save(profB);

                Professor profC = new Professor();
                profC.setNome("Prof. Stephen Hawking");
                profC.setEmail("stephen@hawking.com");
                profC.setSenha(passwordEncoder.encode("senha123"));
                profC.setDataCadastro(hoje);
                profC.setPrecoPorHora(200.00);
                profC.setCategorias(Set.of(catMat, catFis));
                profC = professorRepository.save(profC);

                Professor profD = new Professor();
                profD.setNome("Prof. Marie Curie");
                profD.setEmail("marie@curie.com");
                profD.setSenha(passwordEncoder.encode("senha123"));
                profD.setDataCadastro(hoje);
                profD.setPrecoPorHora(150.00);
                profD.setCategorias(Set.of(catQui, catFis));
                profD.setFotoUrl("https://exemplo.com/fotos/marie.png");
                profD = professorRepository.save(profD);

                Professor profE = new Professor();
                profE.setNome("Prof. Heródoto");
                profE.setEmail("herodoto@grecia.com");
                profE.setSenha(passwordEncoder.encode("senha123"));
                profE.setDataCadastro(hoje);
                profE.setPrecoPorHora(80.00);
                profE.setCategorias(Set.of(catHist));
                profE = professorRepository.save(profE);

                List<Professor> professores = List.of(profA, profB, profC, profD, profE);

                Aluno alunoA = new Aluno();
                alunoA.setNome("Aluno A (João)");
                alunoA.setEmail("aluno.a@teste.com");
                alunoA.setSenha(passwordEncoder.encode("senha123"));
                alunoA.setDataCadastro(hoje);
                alunoA.setFotoUrl("https://exemplo.com/fotos/joao.png");
                alunoA = alunoRepository.save(alunoA);

                Aluno alunoB = new Aluno();
                alunoB.setNome("Aluna B (Maria)");
                alunoB.setEmail("aluno.b@teste.com");
                alunoB.setSenha(passwordEncoder.encode("senha123"));
                alunoB.setDataCadastro(hoje);
                alunoB = alunoRepository.save(alunoB);

                Aluno alunoC = new Aluno();
                alunoC.setNome("Aluno C (Carlos)");
                alunoC.setEmail("aluno.c@teste.com");
                alunoC.setSenha(passwordEncoder.encode("senha123"));
                alunoC.setDataCadastro(hoje);
                alunoC = alunoRepository.save(alunoC);

                Aluno alunoD = new Aluno();
                alunoD.setNome("Aluna D (Daniela)");
                alunoD.setEmail("aluno.d@teste.com");
                alunoD.setSenha(passwordEncoder.encode("senha123"));
                alunoD.setDataCadastro(hoje);
                alunoD = alunoRepository.save(alunoD);

                Aluno alunoE = new Aluno();
                alunoE.setNome("Aluno E (Einstein)");
                alunoE.setEmail("albert@teste.com");
                alunoE.setSenha(passwordEncoder.encode("senha123"));
                alunoE.setDataCadastro(hoje);
                alunoE = alunoRepository.save(alunoE);

                Aluno alunoF = new Aluno();
                alunoF.setNome("Aluno F (Freud)");
                alunoF.setEmail("sigmund@teste.com");
                alunoF.setSenha(passwordEncoder.encode("senha123"));
                alunoF.setDataCadastro(hoje);
                alunoF = alunoRepository.save(alunoF);

                Aluno alunoG = new Aluno();
                alunoG.setNome("Aluno G (Galileu)");
                alunoG.setEmail("galileu@teste.com");
                alunoG.setSenha(passwordEncoder.encode("senha123"));
                alunoG.setDataCadastro(hoje);
                alunoG = alunoRepository.save(alunoG);

                Aluno alunoH = new Aluno();
                alunoH.setNome("Aluna H (Hipatia)");
                alunoH.setEmail("hipatia@teste.com");
                alunoH.setSenha(passwordEncoder.encode("senha123"));
                alunoH.setDataCadastro(hoje);
                alunoH = alunoRepository.save(alunoH);

                Aluno alunoI = new Aluno();
                alunoI.setNome("Aluno I (Isaac)");
                alunoI.setEmail("isaac@teste.com");
                alunoI.setSenha(passwordEncoder.encode("senha123"));
                alunoI.setDataCadastro(hoje);
                alunoI = alunoRepository.save(alunoI);

                Aluno alunoJ = new Aluno();
                alunoJ.setNome("Aluna J (Jasmin)");
                alunoJ.setEmail("jasmin@teste.com");
                alunoJ.setSenha(passwordEncoder.encode("senha123"));
                alunoJ.setDataCadastro(hoje);
                alunoJ = alunoRepository.save(alunoJ);

                List<Aluno> alunos = List.of(alunoA, alunoB, alunoC, alunoD, alunoE, alunoF, alunoG, alunoH, alunoI,
                                alunoJ);

                disponibilidadeRepository.saveAll(List.of(
                                new Disponibilidade(profA, DayOfWeek.MONDAY, LocalTime.of(14, 0), LocalTime.of(18, 0)),
                                new Disponibilidade(profA, DayOfWeek.WEDNESDAY, LocalTime.of(9, 0),
                                                LocalTime.of(12, 0)),
                                new Disponibilidade(profB, DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(13, 0)),
                                new Disponibilidade(profB, DayOfWeek.FRIDAY, LocalTime.of(15, 0), LocalTime.of(18, 0)),
                                new Disponibilidade(profC, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(12, 0)),
                                new Disponibilidade(profC, DayOfWeek.TUESDAY, LocalTime.of(8, 0), LocalTime.of(12, 0)),
                                new Disponibilidade(profD, DayOfWeek.THURSDAY, LocalTime.of(10, 0),
                                                LocalTime.of(16, 0)),
                                new Disponibilidade(profE, DayOfWeek.FRIDAY, LocalTime.of(9, 0), LocalTime.of(12, 0))));

                LocalDateTime inicioBloqueioA = agora.plusDays(7).withHour(14).withMinute(0);
                LocalDateTime fimBloqueioA = inicioBloqueioA.plusHours(2);
                bloqueioHorarioRepository
                                .save(new BloqueioHorario(profA, inicioBloqueioA, fimBloqueioA, "Consulta Médica"));

                LocalDateTime inicioBloqueioB = agora.plusDays(10).withHour(10).withMinute(0);
                LocalDateTime fimBloqueioB = inicioBloqueioB.plusHours(3);
                bloqueioHorarioRepository.save(new BloqueioHorario(profB, inicioBloqueioB, fimBloqueioB, "Dentista"));

                for (int i = 0; i < 20; i++) {
                        Aluno aluno = alunos.get(i % alunos.size());
                        Professor prof = professores.get(i % professores.size());
                        Categoria cat = prof.getCategorias().stream().findFirst().orElse(catProg);
                        LocalDateTime dt = agora.minusDays(i + 1).withHour(10 + (i % 5));

                        Aula aula = new Aula();
                        aula.setProfessor(prof);
                        aula.setCategoria(cat);
                        aula.setTitulo("Aula " + i + " de " + cat.getTitulo() + " (Passada)");
                        aula.setDataHora(dt);
                        aula.setDataHoraFim(dt.plusHours(1));
                        aula.setPreco(prof.getPrecoPorHora());
                        aula.setLocal(i % 2 == 0 ? "https://meet.google.com/exemplo" : "Sala 10B");
                        aula = aulaRepository.save(aula);

                        SolicitacaoAula solic = new SolicitacaoAula();
                        solic.setAluno(aluno);
                        solic.setProfessor(prof);
                        solic.setCategoria(cat);
                        solic.setDataSolicitacao(dt);
                        solic.setStatus("ACEITA");
                        solic = solicitacaoAulaRepository.save(solic);

                        Agendamento ag = new Agendamento();
                        ag.setAluno(aluno);
                        ag.setAula(aula);
                        ag.setSolicitacao(solic);
                        ag.setDataAgendamento(dt.minusDays(1));
                        ag.setStatus("REALIZADO");
                        agendamentoRepository.save(ag);

                        if (i < 10) {
                                Avaliacao avalAluno = new Avaliacao();
                                avalAluno.setProfessor(prof);
                                avalAluno.setAluno(aluno);
                                avalAluno.setAula(aula);
                                avalAluno.setNota(4.0f + (i % 2));
                                avalAluno.setComentario(
                                                "Esta foi uma aula " + (i % 2 == 0 ? "boa" : "excelente") + "!");
                                avalAluno.setAvaliador(TipoAvaliador.ALUNO);
                                avaliacaoRepository.save(avalAluno);

                                Avaliacao avalProf = new Avaliacao();
                                avalProf.setProfessor(prof);
                                avalProf.setAluno(aluno);
                                avalProf.setAula(aula);
                                avalProf.setNota(5.0f);
                                avalProf.setComentario("Aluno participativo.");
                                avalProf.setAvaliador(TipoAvaliador.PROFESSOR);
                                avaliacaoRepository.save(avalProf);
                        }
                }

                for (int i = 0; i < 10; i++) {
                        Aluno aluno = alunos.get(i % alunos.size());
                        Professor prof = professores.get(i % professores.size());
                        Categoria cat = prof.getCategorias().stream().findFirst().orElse(catProg);
                        LocalDateTime dt = agora.plusDays(i + 2).withHour(11 + (i % 4));

                        Aula aula = new Aula();
                        aula.setProfessor(prof);
                        aula.setCategoria(cat);
                        aula.setTitulo("Aula " + i + " de " + cat.getTitulo() + " (Futura)");
                        aula.setDataHora(dt);
                        aula.setDataHoraFim(dt.plusHours(1));
                        aula.setPreco(prof.getPrecoPorHora());
                        aula.setLocal("Link do Meet (a confirmar)");
                        aula = aulaRepository.save(aula);

                        SolicitacaoAula solic = new SolicitacaoAula();
                        solic.setAluno(aluno);
                        solic.setProfessor(prof);
                        solic.setCategoria(cat);
                        solic.setDataSolicitacao(dt);
                        solic.setStatus("ACEITA");
                        solic = solicitacaoAulaRepository.save(solic);

                        Agendamento ag = new Agendamento();
                        ag.setAluno(aluno);
                        ag.setAula(aula);
                        ag.setSolicitacao(solic);
                        ag.setDataAgendamento(agora.minusHours(i + 1));
                        ag.setStatus("CONFIRMADO");
                        agendamentoRepository.save(ag);
                }

                for (int i = 0; i < 5; i++) {
                        Aluno aluno = alunos.get((i + 5) % alunos.size());
                        Professor prof = professores.get(i % professores.size());
                        Categoria cat = prof.getCategorias().stream().findFirst().orElse(catProg);

                        SolicitacaoAula solic = new SolicitacaoAula();
                        solic.setAluno(aluno);
                        solic.setProfessor(prof);
                        solic.setCategoria(cat);
                        solic.setDataSolicitacao(agora.plusDays(10 + i).withHour(15));
                        solic.setDetalhes("Solicitação pendente de teste (" + i + ")");
                        solic.setStatus("PENDENTE");
                        solicitacaoAulaRepository.save(solic);

                        notificacaoRepository.save(new Notificacao(prof, "Nova solicitação de " + aluno.getNome(),
                                        "NOVA_SOLICITACAO", "/solicitacoes"));
                }

                for (int i = 0; i < 3; i++) {
                        Aluno aluno = alunos.get(i % alunos.size());
                        Professor prof = professores.get(i % professores.size());
                        Categoria cat = prof.getCategorias().stream().findFirst().orElse(catProg);

                        SolicitacaoAula solic = new SolicitacaoAula();
                        solic.setAluno(aluno);
                        solic.setProfessor(prof);
                        solic.setCategoria(cat);
                        solic.setDataSolicitacao(agora.plusDays(1).withHour(11 + i));
                        solic.setDetalhes("Solicitação recusada de teste (" + i + ")");
                        solic.setStatus("RECUSADA");
                        solicitacaoAulaRepository.save(solic);

                        Notificacao notif = new Notificacao(aluno,
                                        "Sua solicitação para " + prof.getNome() + " foi recusada.",
                                        "SOLICITACAO_RECUSADA", "/meus-agendamentos");
                        notif.setLida(true);
                        notificacaoRepository.save(notif);
                }

                Mensagem msg1 = new Mensagem();
                msg1.setRemetente(alunoA);
                msg1.setDestinatario(profA);
                msg1.setConteudo("Olá Prof. Alan, quando podemos marcar a aula de Programação?");
                msg1.setDataEnvio(agora.minusHours(2));
                mensagemRepository.save(msg1);

                Mensagem msg2 = new Mensagem();
                msg2.setRemetente(profA);
                msg2.setDestinatario(alunoA);
                msg2.setConteudo(
                                "Olá João. Por favor, envie uma solicitação formal pela plataforma para quarta-feira às 10h.");
                msg2.setDataEnvio(agora.minusHours(1));
                msg2.setLida(false);
                mensagemRepository.save(msg2);

                Mensagem msg3 = new Mensagem();
                msg3.setRemetente(alunoC);
                msg3.setDestinatario(profC);
                msg3.setConteudo("Professor Hawking, estou ansioso pela nossa aula sobre buracos negros!");
                msg3.setDataEnvio(agora.minusMinutes(30));
                msg3.setLida(false);
                mensagemRepository.save(msg3);

                System.out.println("--- [DATA SEEDER FINALIZADO] ---");
        }
}