package com.estudiantes.estudiantes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "curso") // Tabla
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCurso;

    @Column(nullable = false)
    private String nombreCurso;

    @OneToMany(mappedBy = "curso")
    @JsonIgnore // Evita la recursión infinita
    private List<Estudiante> estudiantes;

}

