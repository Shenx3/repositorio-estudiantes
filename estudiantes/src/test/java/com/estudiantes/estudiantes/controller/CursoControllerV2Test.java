package com.estudiantes.estudiantes.controller;

import com.estudiantes.estudiantes.assemblers.CursoAssembler;
import com.estudiantes.estudiantes.model.Curso;
import com.estudiantes.estudiantes.model.CursoModel;
import com.estudiantes.estudiantes.service.CursoService;
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

@WebMvcTest(CursoControllerV2.class)
public class CursoControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CursoService cursoService;

    @MockBean
    private CursoAssembler cursoAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Curso curso;
    private CursoModel cursoModel;

    @BeforeEach
    void setUp() {
        curso = new Curso();
        curso.setIdCurso(1);
        curso.setNombreCurso("Matemáticas");

        cursoModel = new CursoModel();
        cursoModel.setIdCurso(1);
        cursoModel.setNombreCurso("Matemáticas");
        // Añadir enlaces al modelo (simulamos lo que haría el assembler)
        cursoModel.add(linkTo(methodOn(CursoControllerV2.class).buscarCurso(1)).withSelfRel());
        cursoModel.add(linkTo(methodOn(CursoControllerV2.class).listarCursos()).withRel("cursos"));
    }

    @Test
    public void testListarCursos() throws Exception {
        List<Curso> cursos = Collections.singletonList(curso);
        List<CursoModel> cursoModels = Collections.singletonList(cursoModel);
        CollectionModel<CursoModel> collectionModel = CollectionModel.of(cursoModels,
                linkTo(methodOn(CursoControllerV2.class).listarCursos()).withSelfRel());

        when(cursoService.findAll()).thenReturn(cursos);
        when(cursoAssembler.toModel(any(Curso.class))).thenReturn(cursoModel);

        mockMvc.perform(get("/api/v2/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.cursoModelList[0].idCurso").value(1)) // Cambiado a cursoModelList
                .andExpect(jsonPath("$._embedded.cursoModelList[0].nombreCurso").value("Matemáticas"))
                .andExpect(jsonPath("$._embedded.cursoModelList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.cursoModelList[0]._links.cursos.href").exists());
    }

    @Test
    public void testBuscarCurso() throws Exception {
        when(cursoService.findById(1)).thenReturn(curso);
        when(cursoAssembler.toModel(curso)).thenReturn(cursoModel);

        mockMvc.perform(get("/api/v2/cursos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCurso").value(1))
                .andExpect(jsonPath("$.nombreCurso").value("Matemáticas"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.cursos.href").exists());
    }

    @Test
    public void testGuardarCurso() throws Exception {
        when(cursoService.save(any(Curso.class))).thenReturn(curso);

        mockMvc.perform(post("/api/v2/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(curso)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCurso").value(1))
                .andExpect(jsonPath("$.nombreCurso").value("Matemáticas"));
    }

    @Test
    public void testActualizarCurso() throws Exception {
        when(cursoService.findById(1)).thenReturn(curso);
        when(cursoService.save(any(Curso.class))).thenReturn(curso);

        mockMvc.perform(put("/api/v2/cursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(curso)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCurso").value(1))
                .andExpect(jsonPath("$.nombreCurso").value("Matemáticas"));
    }

    @Test
    public void testEliminarCurso() throws Exception {
        doNothing().when(cursoService).delete(1);

        mockMvc.perform(delete("/api/v2/cursos/1"))
                .andExpect(status().isNoContent());

        verify(cursoService, times(1)).delete(1);
    }
}