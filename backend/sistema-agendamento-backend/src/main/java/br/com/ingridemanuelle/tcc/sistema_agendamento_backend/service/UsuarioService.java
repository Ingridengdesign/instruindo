package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.AlunoPerfilDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.CadastroAlunoDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.CadastroProfessorDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.ProfessorPerfilDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.ResetarSenhaDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.SolicitarResetSenhaDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.UpdateAlunoDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.UpdateProfessorDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Aluno;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Categoria;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Professor;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Usuario;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.AlunoRepository;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.CategoriaRepository;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.ProfessorRepository;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.Optional;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public void cadastrarAluno(CadastroAlunoDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {

            throw new RuntimeException("Erro: E-mail já cadastrado!");
        }

        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());

        Aluno novoAluno = new Aluno();
        novoAluno.setNome(dto.getNome());
        novoAluno.setEmail(dto.getEmail());
        novoAluno.setSenha(senhaCriptografada);
        novoAluno.setDataCadastro(LocalDate.now());

        alunoRepository.save(novoAluno);
    }

    public void cadastrarProfessor(CadastroProfessorDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Erro: E-mail já cadastrado!");
        }

        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());

        Professor novoProfessor = new Professor();
        novoProfessor.setNome(dto.getNome());
        novoProfessor.setEmail(dto.getEmail());
        novoProfessor.setSenha(senhaCriptografada);
        novoProfessor.setDataCadastro(LocalDate.now());
        novoProfessor.setPrecoPorHora(dto.getPrecoPorHora());

        Set<Categoria> categorias = new HashSet<>(categoriaRepository.findAllById(dto.getIdCategorias()));
        if (categorias.size() != dto.getIdCategorias().size()) {
            throw new RuntimeException("Erro: Uma ou mais categorias fornecidas são inválidas.");
        }
        novoProfessor.setCategorias(categorias);

        professorRepository.save(novoProfessor);
    }

    @Transactional
    public void atualizarPerfilAluno(UpdateAlunoDTO dto) {

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long usuarioId = usuarioLogado.getIdUsuario();

        Aluno aluno = alunoRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado ou usuário logado não é um aluno."));

        aluno.setNome(dto.getNome());

        if (dto.getNovaSenha() != null && !dto.getNovaSenha().isBlank()) {
            if (dto.getNovaSenha().length() < 6) {
                throw new RuntimeException("A nova senha deve ter no mínimo 6 caracteres.");
            }
            aluno.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
        }

        alunoRepository.save(aluno);
    }

    @Transactional
    public void atualizarPerfilProfessor(UpdateProfessorDTO dto) {

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long usuarioId = usuarioLogado.getIdUsuario();

        Professor professor = professorRepository.findById(usuarioId)
                .orElseThrow(
                        () -> new RuntimeException("Professor não encontrado ou usuário logado não é um professor."));

        professor.setNome(dto.getNome());
        professor.setPrecoPorHora(dto.getPrecoPorHora());

        Set<Categoria> categorias = new HashSet<>(categoriaRepository.findAllById(dto.getIdCategorias()));
        if (categorias.size() != dto.getIdCategorias().size()) {
            throw new RuntimeException("Erro: Uma ou mais categorias fornecidas são inválidas.");
        }

        professor.setCategorias(categorias);

        if (dto.getNovaSenha() != null && !dto.getNovaSenha().isBlank()) {
            if (dto.getNovaSenha().length() < 6) {
                throw new RuntimeException("A nova senha deve ter no mínimo 6 caracteres.");
            }
            professor.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
        }

        professorRepository.save(professor);
    }

    @Transactional(readOnly = true)
    public Object buscarPerfilUsuarioLogado() {

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long usuarioId = usuarioLogado.getIdUsuario();

        Optional<Aluno> alunoOpt = alunoRepository.findById(usuarioId);
        if (alunoOpt.isPresent()) {
            return new AlunoPerfilDTO(alunoOpt.get());
        }

        Optional<Professor> professorOpt = professorRepository.findById(usuarioId);
        if (professorOpt.isPresent()) {

            return new ProfessorPerfilDTO(professorOpt.get());
        }

        throw new RuntimeException("Usuário logado não encontrado como Aluno ou Professor.");
    }

    @Transactional
    public void desativarMeuPerfil() {

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long usuarioId = usuarioLogado.getIdUsuario();

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado."));

        usuario.setAtivo(false);

        usuarioRepository.save(usuario);

    }

    @Transactional
    public void solicitarResetSenha(SolicitarResetSenhaDTO dto) {

        Usuario usuario = usuarioRepository.findByEmailAndAtivoTrue(dto.getEmail())
                .orElse(null);

        if (usuario != null) {

            String token = UUID.randomUUID().toString();
            usuario.setResetToken(token);
            usuario.setResetTokenExpiracao(LocalDateTime.now().plusHours(1));
            usuarioRepository.save(usuario);

            String linkReset = "http://localhost:4200/resetar-senha?token=" + token;
            String assunto = "Recuperação de Senha - Sistema de Agendamento";
            String corpo = String.format(
                    "Olá, %s.\n\nVocê solicitou a recupe    ração da sua senha.\n" +
                            "Clique no link abaixo para definir uma nova senha (expira em 1 hora):\n%s\n\n" +
                            "Se você não solicitou esta alteração, ignore este email.",
                    usuario.getNome(),
                    linkReset);

            emailService.enviarEmailSimples(usuario.getEmail(), assunto, corpo);
        }

    }

    @Transactional
    public void resetarSenha(ResetarSenhaDTO dto) {

        Usuario usuario = usuarioRepository.findByResetTokenAndAtivoTrue(dto.getToken())
                .orElseThrow(() -> new RuntimeException("Token de reset inválido ou expirado."));

        if (usuario.getResetTokenExpiracao().isBefore(LocalDateTime.now())) {

            usuario.setResetToken(null);
            usuario.setResetTokenExpiracao(null);
            usuarioRepository.save(usuario);
            throw new RuntimeException("Token de reset expirado. Solicite um novo.");
        }

        usuario.setSenha(passwordEncoder.encode(dto.getNovaSenha()));

        usuario.setResetToken(null);
        usuario.setResetTokenExpiracao(null);

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void atualizarFotoUrl(Long idUsuario, String urlDaFoto) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado."));

        usuario.setFotoUrl(urlDaFoto);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void removerFotoPerfil() {

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long usuarioId = usuarioLogado.getIdUsuario();

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado."));

        usuario.setFotoUrl(null);

        usuarioRepository.save(usuario);
    }

}