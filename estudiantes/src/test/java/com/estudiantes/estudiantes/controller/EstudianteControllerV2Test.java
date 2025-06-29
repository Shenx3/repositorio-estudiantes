package com.estudiantes.estudiantes.controller;

import com.estudiantes.estudiantes.assemblers.EstudianteAssembler;
import com.estudiantes.estudiantes.model.Curso;
import com.estudiantes.estudiantes.model.Estudiante;
import com.estudiantes.estudiantes.model.EstudianteModel;
import com.estudiantes.estudiantes.service.CursoService;
import com.estudiantes.estudiantes.service.EstudianteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EstudianteControllerV2.class)
public class EstudianteControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstudianteService estudianteService;

    @MockBean
    private CursoService cursoService;

    @MockBean
    private EstudianteAssembler estudianteAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Estudiante estudiante;
    private EstudianteModel estudianteModel;
    private Curso curso;

    @BeforeEach
    void setUp() {
        // Crear curso
        curso = new Curso();
        curso.setIdCurso(1);
        curso.setNombreCurso("Programación");

        // Crear estudiante
        estudiante = new Estudiante();
        estudiante.setIdEstudiante(1);
        estudiante.setRunEstudiante("18.493.402-2");
        estudiante.setNombre("Manolo");
        estudiante.setApellido("Lama");
        estudiante.setEdad(25);
        estudiante.setCurso(curso);

        // Crear modelo de estudiante con HATEOAS
        estudianteModel = new EstudianteModel();
        estudianteModel.setIdEstudiante(1);
        estudianteModel.setRunEstudiante("18.493.402-2");
        estudianteModel.setNombre("Manolo");
        estudianteModel.setApellido("Lama");
        estudianteModel.setEdad(25);

        // Añadir enlaces al modelo (simulamos lo que haría el assembler)
        estudianteModel.add(linkTo(methodOn(EstudianteControllerV2.class).buscarEstudiante(1)).withSelfRel());
        estudianteModel.add(linkTo(methodOn(EstudianteControllerV2.class).listarEstudiantes()).withRel("estudiantes"));
    }

    @Test
    public void testListarEstudiantes() throws Exception {
        List<Estudiante> estudiantes = Collections.singletonList(estudiante);
        List<EstudianteModel> estudianteModels = Collections.singletonList(estudianteModel);
        CollectionModel<EstudianteModel> collectionModel = CollectionModel.of(estudianteModels,
                linkTo(methodOn(EstudianteControllerV2.class).listarEstudiantes()).withSelfRel());

        when(estudianteService.findAll()).thenReturn(estudiantes);
        when(estudianteAssembler.toModel(any(Estudiante.class))).thenReturn(estudianteModel);

        mockMvc.perform(get("/api/v2/estudiantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.estudianteModelList[0].idEstudiante").value(1))
                .andExpect(jsonPath("$._embedded.estudianteModelList[0].runEstudiante").value("18.493.402-2"))
                .andExpect(jsonPath("$._embedded.estudianteModelList[0].nombre").value("Manolo"))
                .andExpect(jsonPath("$._embedded.estudianteModelList[0].apellido").value("Lama"))
                .andExpect(jsonPath("$._embedded.estudianteModelList[0].edad").value(25))
                .andExpect(jsonPath("$._embedded.estudianteModelList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.estudianteModelList[0]._links.estudiantes.href").exists());
    }

    @Test
    public void testBuscarEstudiante() throws Exception {
        when(estudianteService.findById(1)).thenReturn(estudiante);
        when(estudianteAssembler.toModel(estudiante)).thenReturn(estudianteModel);

        mockMvc.perform(get("/api/v2/estudiantes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEstudiante").value(1))
                .andExpect(jsonPath("$.runEstudiante").value("18.493.402-2"))
                .andExpect(jsonPath("$.nombre").value("Manolo"))
                .andExpect(jsonPath("$.apellido").value("Lama"))
                .andExpect(jsonPath("$.edad").value(25))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.estudiantes.href").exists());
    }

    @Test
    public void testGuardarEstudiante() throws Exception {
        when(estudianteService.save(any(Estudiante.class))).thenReturn(estudiante);

        mockMvc.perform(post("/api/v2/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estudiante)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idEstudiante").value(1))
                .andExpect(jsonPath("$.runEstudiante").value("18.493.402-2"))
                .andExpect(jsonPath("$.nombre").value("Manolo"))
                .andExpect(jsonPath("$.apellido").value("Lama"))
                .andExpect(jsonPath("$.edad").value(25));
    }

    @Test
    public void testActualizarEstudiante() throws Exception {
        when(estudianteService.findById(1)).thenReturn(estudiante);
        when(estudianteService.save(any(Estudiante.class))).thenReturn(estudiante);

        mockMvc.perform(put("/api/v2/estudiantes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estudiante)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEstudiante").value(1))
                .andExpect(jsonPath("$.runEstudiante").value("18.493.402-2"))
                .andExpect(jsonPath("$.nombre").value("Manolo"))
                .andExpect(jsonPath("$.apellido").value("Lama"))
                .andExpect(jsonPath("$.edad").value(25));
    }

    @Test
    public void testEliminarEstudiante() throws Exception {
        doNothing().when(estudianteService).delete(1);

        mockMvc.perform(delete("/api/v2/estudiantes/1"))
                .andExpect(status().isNoContent());

        verify(estudianteService, times(1)).delete(1);
    }

    @Test
    public void testAsignarCursoAEstudiante() throws Exception {
        when(estudianteService.findById(1)).thenReturn(estudiante);
        when(cursoService.findById(1)).thenReturn(curso);
        when(estudianteService.save(any(Estudiante.class))).thenReturn(estudiante);
        when(estudianteAssembler.toModel(any(Estudiante.class))).thenReturn(estudianteModel);

        mockMvc.perform(put("/api/v2/estudiantes/1/asignarcurso/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEstudiante").value(1))
                .andExpect(jsonPath("$.nombre").value("Manolo"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.estudiantes.href").exists());
    }

    @Test
    public void testRemoverEstudianteDeCurso() throws Exception {
        when(estudianteService.findById(1)).thenReturn(estudiante);
        estudiante.setCurso(null);
        when(estudianteService.save(any(Estudiante.class))).thenReturn(estudiante);

        mockMvc.perform(delete("/api/v2/estudiantes/1/removercurso"))
                .andExpect(status().isNoContent()); // Espera un 204 No Content

        verify(estudianteService, times(1)).save(any(Estudiante.class));
    }
}