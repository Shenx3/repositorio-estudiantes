package com.estudiantes.estudiantes.model;

// Imports
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Mapeara la clase en la BD
@Table(name = "estudiante") // Nombre de la Tabla
@Data // Genera Automaticamente los Getters, Setters, ToString, equals y hashCode
@NoArgsConstructor // Constructor sin parametros
@AllArgsConstructor // Constructor con parametros
public class Estudiante {

    @Id // Define idEstudiante como PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincrementable
    private int idEstudiante;

    @Column(unique = true, nullable = false) // Crea una columna llamada runEstudiante
    private String runEstudiante;            // dentro de la tabla estudiante (establecida como unica y no se puede saltar)

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private int edad;

    @ManyToOne // Muchos estudiantes pueden estar dentro de un curso
    @JoinColumn(name = "id_curso") // Define la FK
    @JsonBackReference // Evita la recursión infinita al serializar relaciones bidireccionales con JSON
    private Curso curso;
}
