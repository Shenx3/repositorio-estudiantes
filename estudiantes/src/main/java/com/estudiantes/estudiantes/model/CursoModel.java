package com.estudiantes.estudiantes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoModel extends RepresentationModel<CursoModel> {
    private int idCurso;
    private String nombreCurso;
    private List<EstudianteModel> estudiantes;  // ← Usar EstudianteModel, no Estudiante
}
