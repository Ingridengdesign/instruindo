package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.entity;

import jakarta.persistence.*;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(exclude = "professores")
@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategoria;

    @Column(nullable = false, unique = true)
    private String titulo;

    @Column(length = 500)
    private String descricao;

    @ManyToMany(mappedBy = "categorias")
    private Set<Professor> professores;
}