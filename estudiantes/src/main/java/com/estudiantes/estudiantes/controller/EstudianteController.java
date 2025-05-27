package com.estudiantes.estudiantes.controller;

import com.estudiantes.estudiantes.service.CursoService;
import com.estudiantes.estudiantes.model.Curso;
import com.estudiantes.estudiantes.model.Estudiante;
import com.estudiantes.estudiantes.service.EstudianteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // La clase recibira peticiones HTTP y las entregara en JSON
@RequestMapping("/api/v1/estudiantes") // Define la ruta base para todos los endpoints de este controlador
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;
    @Autowired
    private CursoService cursoService;

    @GetMapping // Listar Estudiantes
    public ResponseEntity<List<Estudiante>> listarEstudiantes(){
        List<Estudiante> estudiantes = estudianteService.findAll();
        if(estudiantes.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estudiantes);
    }

    @PostMapping // Guardar Estudiantes
    public ResponseEntity<Estudiante> guardarEstudiante(@RequestBody Estudiante estudiante) {
        Estudiante estudiante1 = estudianteService.save(estudiante);
        return ResponseEntity.status(HttpStatus.CREATED).body(estudiante1);
    }

    @GetMapping("/{id}") // Buscar por ID Estudiantes
    public ResponseEntity<Estudiante> buscarEstudiante(@PathVariable Integer id) {
        try {
            Estudiante estudiante = estudianteService.findById(id);
            return ResponseEntity.ok(estudiante);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}") // Actualizar Estudiantes
    public ResponseEntity<Estudiante> actualizarEstudiante(@PathVariable Integer id, @RequestBody Estudiante estudiante) {
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
    public ResponseEntity<Void> eliminarEstudiante(@PathVariable Long id) {
        try {
            estudianteService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // CURSOS

    @PutMapping("/{id}/asignarcurso/{cursoId}") // Asignar Curso a Estudiante
    public ResponseEntity<Estudiante> asignarCursoAEstudiante(
            @PathVariable Integer id,
            @PathVariable Integer cursoId) {

        try {
            Estudiante estudiante = estudianteService.findById(id);
            Curso curso = cursoService.findById(cursoId);

            estudiante.setCurso(curso);
            estudianteService.save(estudiante);

            return ResponseEntity.ok(estudiante);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{idEstudiante}/removercurso") // Remover Curso a Estudiante
    public ResponseEntity<Void> removerEstudianteDeCurso(
            @PathVariable Integer idEstudiante) {
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
