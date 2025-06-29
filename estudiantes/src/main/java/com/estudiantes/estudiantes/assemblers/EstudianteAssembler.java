package com.estudiantes.estudiantes.assemblers;

import com.estudiantes.estudiantes.controller.EstudianteControllerV2;
import com.estudiantes.estudiantes.model.Estudiante;
import com.estudiantes.estudiantes.model.EstudianteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class EstudianteAssembler implements RepresentationModelAssembler<Estudiante, EstudianteModel> {

    @Autowired
    private CursoAssembler cursoAssembler;

    @Override
    public EstudianteModel toModel(Estudiante estudiante) {
        EstudianteModel model = new EstudianteModel();
        model.setIdEstudiante(estudiante.getIdEstudiante());
        model.setRunEstudiante(estudiante.getRunEstudiante());
        model.setNombre(estudiante.getNombre());
        model.setApellido(estudiante.getApellido());
        model.setEdad(estudiante.getEdad());

        if (estudiante.getCurso() != null) {
            model.setCurso(cursoAssembler.toModel(estudiante.getCurso()));
            // Enlace para remover curso de estudiante
            model.add(linkTo(methodOn(EstudianteControllerV2.class)
                    .removerEstudianteDeCurso(estudiante.getIdEstudiante())).withRel("removerCurso"));
            // Enlace para cambiar de curso al estudiante
            model.add(linkTo(methodOn(EstudianteControllerV2.class)
                    .asignarCursoAEstudiante(estudiante.getIdEstudiante(), 0)).withRel("cambiarCurso")); // Asumiendo que el ID del curso será un parámetro en el PUT
        } else {
            // Enlace para asignar un curso al estudiante
            model.add(linkTo(methodOn(EstudianteControllerV2.class)
                    .asignarCursoAEstudiante(estudiante.getIdEstudiante(), 0)).withRel("asignarCurso")); // Asumiendo que el ID del curso será un parámetro en el PUT
        }

        // Enlaces
        model.add(linkTo(methodOn(EstudianteControllerV2.class)
                .buscarEstudiante(estudiante.getIdEstudiante())).withSelfRel());

        model.add(linkTo(methodOn(EstudianteControllerV2.class)
                .listarEstudiantes()).withRel("estudiantes"));

        return model;
    }
}