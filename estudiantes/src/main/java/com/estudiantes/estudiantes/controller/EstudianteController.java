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


}
