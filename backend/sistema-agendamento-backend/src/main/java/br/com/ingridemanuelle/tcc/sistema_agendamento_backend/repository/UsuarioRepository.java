package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);

    Optional<Usuario> findByEmailAndAtivoTrue(String email);

    Optional<Usuario> findByIdUsuarioAndAtivoTrue(Long id);

    Optional<Usuario> findByResetTokenAndAtivoTrue(String resetToken);

}
