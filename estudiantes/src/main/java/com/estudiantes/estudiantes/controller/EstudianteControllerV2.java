package com.estudiantes.estudiantes.controller;

import com.estudiantes.estudiantes.assemblers.EstudianteAssembler;
import com.estudiantes.estudiantes.model.EstudianteModel;
import com.estudiantes.estudiantes.service.CursoService;
import com.estudiantes.estudiantes.model.Curso;
import com.estudiantes.estudiantes.model.Estudiante;
import com.estudiantes.estudiantes.service.EstudianteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.List;
import java.util.stream.Collectors;

@RestController // La clase recibira peticiones HTTP y las entregara en JSON
@RequestMapping("/api/v2/estudiantes") // Define la ruta base para todos los endpoints de este controlador
public class EstudianteControllerV2 {

    @Autowired
    private EstudianteService estudianteService;
    @Autowired
    private CursoService cursoService;
    @Autowired
    private EstudianteAssembler estudianteAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EstudianteModel>> listarEstudiantes() {
        List<EstudianteModel> models = estudianteService.findAll().stream()
                .map(estudianteAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EstudianteModel> collection = CollectionModel.of(models)
                .add(linkTo(methodOn(EstudianteController.class).listarEstudiantes()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @PostMapping // Guardar Estudiantes
    public ResponseEntity<Estudiante> guardarEstudiante(@RequestBody Estudiante estudiante) {
        Estudiante estudiante1 = estudianteService.save(estudiante);
        return ResponseEntity.status(HttpStatus.CREATED).body(estudiante1);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteModel> buscarEstudiante(@PathVariable int id) {
        Estudiante estudiante = estudianteService.findById(id);
        EstudianteModel model = estudianteAssembler.toModel(estudiante);
        return ResponseEntity.ok(model);
    }

    @PutMapping("/{id}") // Actualizar Estudiantes
    public ResponseEntity<Estudiante> actualizarEstudiante(@PathVariable int id, @RequestBody Estudiante estudiante) {
        try {
            Estudiante estudianteActual = estudianteService.findById(id);
            estudianteActual.setRunEstudiante(estudiante.getRunEstudiante());
            estudianteActual.setNombre(estudiante.getNombre());
            estudianteActual.setApellido(estudiante.getApellido());
            estudianteActual.setEdad(estudiante.getEdad());
            estudianteActual.setCurso(estudiante.getCurso());

            estudianteService.save(estudianteActual);
            return ResponseEntity.ok(estudianteActual);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}") // Eliminar Estudiantes
    public ResponseEntity<Void> eliminarEstudiante(@PathVariable int id) {
        try {
            estudianteService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // CURSOS

    @PutMapping("/{id}/asignarcurso/{cursoId}")
    public ResponseEntity<EstudianteModel> asignarCursoAEstudiante(
            @PathVariable int id,
            @PathVariable int cursoId) {

        Estudiante estudiante = estudianteService.findById(id);
        Curso curso = cursoService.findById(cursoId);

        estudiante.setCurso(curso);
        estudianteService.save(estudiante);

        return ResponseEntity.ok(estudianteAssembler.toModel(estudiante));
    }

    @DeleteMapping("/{idEstudiante}/removercurso") // Remover Curso a Estudiante
    public ResponseEntity<Void> removerEstudianteDeCurso(
            @PathVariable int idEstudiante) {
        try {
            Estudiante estudiante = estudianteService.findById(idEstudiante);
            estudiante.setCurso(null);
            estudianteService.save(estudiante);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

