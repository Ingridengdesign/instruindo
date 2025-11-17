package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Notificacao;
import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByUsuarioDestinoOrderByDataCriacaoDesc(Usuario usuarioDestino);

    List<Notificacao> findByUsuarioDestinoAndLidaIsFalseOrderByDataCriacaoDesc(Usuario usuarioDestino);

    long countByUsuarioDestinoAndLidaIsFalse(Usuario usuarioDestino);

    @Modifying
    @Transactional
    @Query("UPDATE Notificacao n SET n.lida = true WHERE n.usuarioDestino = :usuario AND n.lida = false")
    int marcarTodasComoLidas(@Param("usuario") Usuario usuario);
}