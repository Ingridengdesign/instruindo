package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MensagemRepository extends JpaRepository<Mensagem, Long> {

    @Query("SELECT m FROM Mensagem m " +
            "WHERE (m.remetente.idUsuario = :idUsuarioLogado AND m.destinatario.idUsuario = :idOutroUsuario) " +
            "OR (m.remetente.idUsuario = :idOutroUsuario AND m.destinatario.idUsuario = :idUsuarioLogado) " +
            "ORDER BY m.dataEnvio ASC")
    List<Mensagem> findHistoricoConversa(
            @Param("idUsuarioLogado") Long idUsuarioLogado,
            @Param("idOutroUsuario") Long idOutroUsuario);

    @Modifying
    @Transactional
    @Query("UPDATE Mensagem m SET m.lida = true " +
            "WHERE m.remetente.idUsuario = :idRemetente " +
            "AND m.destinatario.idUsuario = :idDestinatarioLogado " +
            "AND m.lida = false")
    void marcarMensagensComoLidas(
            @Param("idRemetente") Long idRemetente,
            @Param("idDestinatarioLogado") Long idDestinatarioLogado);

}