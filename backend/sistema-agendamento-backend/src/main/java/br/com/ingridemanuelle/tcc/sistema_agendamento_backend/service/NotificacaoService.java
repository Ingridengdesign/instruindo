package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Notificacao;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Usuario;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.NotificacaoRepository;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository.UsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public void criarNotificacao(Usuario usuarioDestino, String mensagem, String tipo, String link) {
        Notificacao notificacao = new Notificacao(usuarioDestino, mensagem, tipo, link);
        notificacaoRepository.save(notificacao);
    }

    @Transactional(readOnly = true)
    public List<Notificacao> buscarMinhasNotificacoes(boolean apenasNaoLidas) {
        Usuario usuarioLogado = getUsuarioLogado();
        if (apenasNaoLidas) {
            return notificacaoRepository.findByUsuarioDestinoAndLidaIsFalseOrderByDataCriacaoDesc(
                    usuarioLogado);
        } else {
            return notificacaoRepository.findByUsuarioDestinoOrderByDataCriacaoDesc(usuarioLogado);
        }
    }

    @Transactional
    public void marcarComoLida(Long idNotificacao) {
        Usuario usuarioLogado = getUsuarioLogado();
        Notificacao notificacao = notificacaoRepository
                .findById(idNotificacao)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada."));

        if (!notificacao.getUsuarioDestino().getIdUsuario().equals(usuarioLogado.getIdUsuario())) {
            throw new RuntimeException("Acesso negado: Notificação não pertence a este utilizador.");
        }

        if (!notificacao.isLida()) {
            notificacao.setLida(true);
            notificacaoRepository.save(notificacao);
        }
    }

    @Transactional
    public int marcarTodasMinhasComoLidas() {
        Usuario usuarioLogado = getUsuarioLogado();
        return notificacaoRepository.marcarTodasComoLidas(usuarioLogado);
    }

    @Transactional(readOnly = true)
    public long contarMinhasNaoLidas() {
        Usuario usuarioLogado = getUsuarioLogado();
        return notificacaoRepository.countByUsuarioDestinoAndLidaIsFalse(usuarioLogado);
    }

    private Usuario getUsuarioLogado() {
        Usuario principal = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuarioRepository
                .findById(principal.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado no banco."));
    }
}