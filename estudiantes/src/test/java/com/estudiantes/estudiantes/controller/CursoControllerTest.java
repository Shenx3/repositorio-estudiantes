package com.estudiantes.estudiantes.controller;

import com.estudiantes.estudiantes.model.Curso;
import com.estudiantes.estudiantes.model.Estudiante;
import com.estudiantes.estudiantes.service.CursoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean; // Ignorar advertencia
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CursoController.class) // Indica que estamos probando CursoController
public class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Mock del servicio de Curso
    private CursoService cursoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Curso curso; // Objeto Curso de prueba

    @BeforeEach // Se ejecuta antes de cada test
    void setUp() {
        curso = new Curso();
        curso.setIdCurso(1);
        curso.setNombreCurso("Matemáticas");

    }

    @Test
    public void testGetAllCursos() throws Exception {
        // Configura el mock para devolver una lista con el curso de prueba
        when(cursoService.findAll()).thenReturn(List.of(curso));

        mockMvc.perform(get("/api/v1/cursos"))
                .andExpect(status().isOk()) // Espera un 200 OK
                .andExpect(jsonPath("$[0].idCurso").value(1))
                .andExpect(jsonPath("$[0].nombreCurso").value("Matemáticas"));
    }

    @Test
    public void testGetCursoById() throws Exception {
        when(cursoService.findById(1)).thenReturn(curso);
        mockMvc.perform(get("/api/v1/cursos/1"))

                // Verficacion status
                .andExpect(status().isOk())
                // Verificaciones del curso
                .andExpect(jsonPath("$.idCurso").value(1))
                .andExpect(jsonPath("$.nombreCurso").value("Matemáticas"));

    }

    @Test
    public void testCreateCurso() throws Exception {
        when(cursoService.save(any(Curso.class))).thenReturn(curso);

        mockMvc.perform(post("/api/v1/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(curso)))
                // Verficacion status
                .andExpect(status().isCreated())
                // Verificaciones del curso
                .andExpect(jsonPath("$.idCurso").value(1))
                .andExpect(jsonPath("$.nombreCurso").value("Matemáticas"));


    }

    @Test
    public void testUpdateCurso() throws Exception {

        when(cursoService.findById(eq(1))).thenReturn(curso);
        when(cursoService.save(any(Curso.class))).thenReturn(curso);

        mockMvc.perform(put("/api/v1/cursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(curso)))
                // Verficacion status
                .andExpect(status().isOk())
                // Verificaciones del curso
                .andExpect(jsonPath("$.idCurso").value(1))
                .andExpect(jsonPath("$.nombreCurso").value("Matemáticas"));
    }

    @Test
    public void testDeleteCurso() throws Exception {
        doNothing().when(cursoService).delete(eq(1));

        mockMvc.perform(delete("/api/v1/cursos/1"))
                .andExpect(status().isNoContent()); // Verifica que el estado sea un 204

        verify(cursoService, times(1)).delete(eq(1));
    }
}