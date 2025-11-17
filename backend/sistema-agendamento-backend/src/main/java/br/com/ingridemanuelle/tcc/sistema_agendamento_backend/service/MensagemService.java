package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.MensagemDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.dto.NovaMensagemDTO;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Mensagem;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Usuario;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.MensagemRepository;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MensagemService {

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public MensagemDTO enviarMensagem(NovaMensagemDTO dto) {

        Usuario remetente = getUsuarioLogadoCompleto();

        Usuario destinatario = usuarioRepository.findByIdUsuarioAndAtivoTrue(dto.getIdDestinatario())
                .orElseThrow(() -> new RuntimeException("Destinatário ativo não encontrado."));

        if (remetente.getIdUsuario().equals(destinatario.getIdUsuario())) {
            throw new RuntimeException("Não pode enviar mensagens para si mesmo.");
        }

        if (!destinatario.isAtivo()) {
            throw new RuntimeException("Não é possível enviar mensagem: este utilizador está desativado.");
        }

        Mensagem novaMensagem = new Mensagem();
        novaMensagem.setRemetente(remetente);
        novaMensagem.setDestinatario(destinatario);
        novaMensagem.setConteudo(dto.getConteudo());

        Mensagem mensagemSalva = mensagemRepository.save(novaMensagem);

        String assuntoEmail = String.format("Você tem uma nova mensagem de %s.", remetente.getNome());
        String corpoEmail = String.format(
                "Olá, %s.\n\nVocê recebeu uma nova mensagem na plataforma.\n\n" +
                        "De: %s\n" +
                        "Mensagem: \"%s\"\n\n" +
                        "Acesse a plataforma para responder.",
                destinatario.getNome(),
                remetente.getNome(),
                novaMensagem.getConteudo());
        emailService.enviarEmailSimples(destinatario.getEmail(), assuntoEmail, corpoEmail);


        return new MensagemDTO(mensagemSalva);
    }

    @Transactional(readOnly = true)
    public List<MensagemDTO> buscarHistoricoConversa(Long idOutroUsuario) {

        Usuario usuarioLogado = getUsuarioLogadoCompleto();

        List<Mensagem> mensagens = mensagemRepository.findHistoricoConversa(
                usuarioLogado.getIdUsuario(),
                idOutroUsuario);

        return mensagens.stream()
                .map(MensagemDTO::new)
                .collect(Collectors.toList());
    }

    private Usuario getUsuarioLogadoCompleto() {
        Usuario principal = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuarioRepository.findById(principal.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Utilizador logado não encontrado no banco."));
    }

    @Transactional 
    public void marcarConversaComoLida(Long idOutroUsuario) {

        Usuario usuarioLogado = getUsuarioLogadoCompleto();
        Long idDestinatarioLogado = usuarioLogado.getIdUsuario();

        Long idRemetente = idOutroUsuario;

        mensagemRepository.marcarMensagensComoLidas(idRemetente, idDestinatarioLogado);
    }
}