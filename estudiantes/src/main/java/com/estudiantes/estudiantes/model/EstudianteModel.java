package com.estudiantes.estudiantes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteModel extends RepresentationModel<EstudianteModel> {
    private int idEstudiante;
    private String runEstudiante;
    private String nombre;
    private String apellido;
    private int edad;
    private CursoModel curso;

}
