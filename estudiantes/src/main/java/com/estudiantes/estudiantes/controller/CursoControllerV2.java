package com.estudiantes.estudiantes.controller;


import com.estudiantes.estudiantes.assemblers.CursoAssembler;
import com.estudiantes.estudiantes.model.Curso;
import com.estudiantes.estudiantes.model.CursoModel;
import com.estudiantes.estudiantes.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/cursos")
public class CursoControllerV2 {

    @Autowired
    private CursoService cursoService;
    @Autowired
    private CursoAssembler cursoAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<CursoModel>> listarCursos() {
        List<CursoModel> models = cursoService.findAll().stream()
                .map(cursoAssembler::toModel)  // Convierte cada Curso a CursoModel con enlaces
                .collect(Collectors.toList());

        CollectionModel<CursoModel> collection = CollectionModel.of(models)
                .add(linkTo(methodOn(CursoController.class).listarCursos()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @PostMapping
    public ResponseEntity<Curso> guardarCurso(@RequestBody Curso curso) {
        Curso cursoNuevo = cursoService.save(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoNuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> actualizarCurso(@PathVariable Integer id, @RequestBody Curso curso) {
        try {
            Curso cursoActual = cursoService.findById(id);
            cursoActual.setNombreCurso(curso.getNombreCurso());

            cursoService.save(cursoActual);
            return ResponseEntity.ok(cursoActual);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoModel> buscarCurso(@PathVariable Integer id) {
        Curso curso = cursoService.findById(id);
        CursoModel model = cursoAssembler.toModel(curso);
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCurso(@PathVariable Integer id) {
        try {
            cursoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}




