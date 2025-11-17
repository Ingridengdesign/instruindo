package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "professor")
@Entity
@Table(name = "bloqueios_horario")
public class BloqueioHorario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_professor", nullable = false)
    private Professor professor;

    @Column(nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(nullable = false)
    private LocalDateTime dataHoraFim;

    @Column(length = 255)
    private String motivo;

    public BloqueioHorario() {
    }

    public BloqueioHorario(Professor professor, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim,
            String motivo) {
        this.professor = professor;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.motivo = motivo;
    }
}