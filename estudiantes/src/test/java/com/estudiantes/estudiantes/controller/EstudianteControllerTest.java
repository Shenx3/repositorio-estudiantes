package com.estudiantes.estudiantes.controller;

import com.estudiantes.estudiantes.model.Curso;
import com.estudiantes.estudiantes.model.Estudiante;
import com.estudiantes.estudiantes.service.CursoService;
import com.estudiantes.estudiantes.service.EstudianteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EstudianteController.class) // Indica que se está probando el controlador de Estudiante
public class EstudianteControllerTest {

    @Autowired // Proporciona una manera de realizar peticiones HTTP en las pruebas
    private MockMvc mockMvc;

    @MockBean // Crea un mock del servicio de Estudiante
    private EstudianteService estudianteService;

    @MockBean
    private CursoService cursoService;

    @Autowired // Se usa para convertir objetos Java a JSON y viceversa
    private ObjectMapper objectMapper;

    private Estudiante estudiante;

    private Curso curso;

    @BeforeEach // Configura un objeto Estudiante de ejemplo antes de cada prueba
    void setUp() {
        // Crear curso
        curso = new Curso();
        curso.setIdCurso(1);
        curso.setNombreCurso("Programación");

        // Crear estudiante
        estudiante = new Estudiante();
        estudiante.setRunEstudiante("18.493.402-2");
        estudiante.setNombre("Manolo");
        estudiante.setApellido("Lama");
        estudiante.setEdad(25);
        estudiante.setCurso(curso);

        // Establecer la relación inversa
        curso.setEstudiantes(List.of(estudiante));
    }

    @Test //
    public void testGetAllEstudiantes() throws Exception {
        // Configura el mock usando el estudiante del setUp (que tiene todos los datos)
        when(estudianteService.findAll()).thenReturn(List.of(estudiante));

        mockMvc.perform(get("/api/v1/estudiantes"))
                .andExpect(status().isOk())
                // Verificaciones del estudiante
                .andExpect(jsonPath("$[0].runEstudiante").value("18.493.402-2"))
                .andExpect(jsonPath("$[0].nombre").value("Manolo"))
                .andExpect(jsonPath("$[0].apellido").value("Lama"))
                .andExpect(jsonPath("$[0].edad").value(25))
                // Verificaciones del curso
                .andExpect(jsonPath("$[0].curso").exists())
                .andExpect(jsonPath("$[0].curso.idCurso").value(1))
                .andExpect(jsonPath("$[0].curso.nombreCurso").value("Programación"));
    }
}
