package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(exclude = "professor")
@Entity
@Table(name = "disponibilidades")
public class Disponibilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDisponibilidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_professor", nullable = false)
    private Professor professor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek diaDaSemana;

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFim;

    public Disponibilidade() {
    }

    public Disponibilidade(
            Professor professor, DayOfWeek diaDaSemana, LocalTime horaInicio, LocalTime horaFim) {
        this.professor = professor;
        this.diaDaSemana = diaDaSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }
}