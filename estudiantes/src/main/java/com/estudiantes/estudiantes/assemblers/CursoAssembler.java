package com.estudiantes.estudiantes.assemblers;

import com.estudiantes.estudiantes.controller.CursoControllerV2;
import com.estudiantes.estudiantes.model.Curso;
import com.estudiantes.estudiantes.model.CursoModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CursoAssembler implements RepresentationModelAssembler<Curso, CursoModel> {

    @Override
    public CursoModel toModel(Curso curso) {
        CursoModel model = new CursoModel();
        model.setIdCurso(curso.getIdCurso());
        model.setNombreCurso(curso.getNombreCurso());

        // Enlaces principales
        model.add(linkTo(methodOn(CursoControllerV2.class)
                .buscarCurso(curso.getIdCurso())).withSelfRel());

        model.add(linkTo(methodOn(CursoControllerV2.class)
                .listarCursos()).withRel("cursos"));

        return model;
    }
}
