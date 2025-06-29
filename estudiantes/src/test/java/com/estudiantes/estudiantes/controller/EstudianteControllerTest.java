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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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
        estudiante.setIdEstudiante(1);
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
                // Verficacion status
                .andExpect(status().isOk())
                // Verificaciones del estudiante
                .andExpect(jsonPath("$[0].idEstudiante").value(1))
                .andExpect(jsonPath("$[0].runEstudiante").value("18.493.402-2"))
                .andExpect(jsonPath("$[0].nombre").value("Manolo"))
                .andExpect(jsonPath("$[0].apellido").value("Lama"))
                .andExpect(jsonPath("$[0].edad").value(25))
                // Verificaciones del curso
                .andExpect(jsonPath("$[0].curso").exists())
                .andExpect(jsonPath("$[0].curso.idCurso").value(1))
                .andExpect(jsonPath("$[0].curso.nombreCurso").value("Programación"));
    }

    @Test
    public void testGetEstudianteById() throws Exception {
        when(estudianteService.findById(1)).thenReturn(estudiante);
        mockMvc.perform(get("/api/v1/estudiantes/1"))

                // Verficacion status
                .andExpect(status().isOk())
                // Verificaciones del estudiante
                .andExpect(jsonPath("$.idEstudiante").value(1))
                .andExpect(jsonPath("$.runEstudiante").value("18.493.402-2"))
                .andExpect(jsonPath("$.nombre").value("Manolo"))
                .andExpect(jsonPath("$.apellido").value("Lama"))
                .andExpect(jsonPath("$.edad").value(25))
                // Verificaciones del curso
                .andExpect(jsonPath("$.curso").exists())
                .andExpect(jsonPath("$.curso.idCurso").value(1))
                .andExpect(jsonPath("$.curso.nombreCurso").value("Programación"));

    }

    @Test
    public void testCreateEstudiante() throws Exception {
        when(estudianteService.save(any(Estudiante.class))).thenReturn(estudiante);

        mockMvc.perform(post("/api/v1/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estudiante)))
                // Verficacion status
                .andExpect(status().isCreated())
                // Verificaciones del estudiante
                .andExpect(jsonPath("$.idEstudiante").value(1))
                .andExpect(jsonPath("$.runEstudiante").value("18.493.402-2"))
                .andExpect(jsonPath("$.nombre").value("Manolo"))
                .andExpect(jsonPath("$.apellido").value("Lama"))
                .andExpect(jsonPath("$.edad").value(25))
                // Verificaciones del curso
                .andExpect(jsonPath("$.curso").exists())
                .andExpect(jsonPath("$.curso.idCurso").value(1))
                .andExpect(jsonPath("$.curso.nombreCurso").value("Programación"));

    }

    @Test
    public void testUpdateEstudiante() throws Exception {

        when(estudianteService.findById(eq(1L))).thenReturn(estudiante);
        when(estudianteService.save(any(Estudiante.class))).thenReturn(estudiante);

        mockMvc.perform(put("/api/v1/estudiantes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estudiante)))
                // Verficacion status
                .andExpect(status().isOk())
                // Verificaciones del estudiante
                .andExpect(jsonPath("$.idEstudiante").value(1))
                .andExpect(jsonPath("$.runEstudiante").value("18.493.402-2"))
                .andExpect(jsonPath("$.nombre").value("Manolo"))
                .andExpect(jsonPath("$.apellido").value("Lama"))
                .andExpect(jsonPath("$.edad").value(25))
                // Verificaciones del curso
                .andExpect(jsonPath("$.curso").exists())
                .andExpect(jsonPath("$.curso.idCurso").value(1))
                .andExpect(jsonPath("$.curso.nombreCurso").value("Programación"));
    }

    @Test
    public void testDeleteEstudiante() throws Exception {
        doNothing().when(estudianteService).delete(eq(1L));

        mockMvc.perform(delete("/api/v1/estudiantes/1"))
                .andExpect(status().isNoContent()); // Verifica que el estado sea un 204

        verify(estudianteService, times(1)).delete(eq(1L));
    }

    // CURSOS

    // Asignar curso a estudiante
    @Test
    public void testAsignarCursoAEstudiante() throws Exception {
        when(estudianteService.findById(eq(1L))).thenReturn(estudiante);
        // Cuando se busque al curso por su ID, se devuelve el objeto 'curso'
        when(cursoService.findById(eq(1L))).thenReturn(curso);
        // Cuando se guarde cualquier estudiante, se devuelve el mismo 'estudiante' (que ya tendrá el curso asignado)
        when(estudianteService.save(any(Estudiante.class))).thenReturn(estudiante);

        // Petición PUT al endpoint
        mockMvc.perform(put("/api/v1/estudiantes/1/asignarcurso/1")) // URL del endpoint
                .andExpect(status().isOk()) // Esperamos un 200 OK
                .andExpect(jsonPath("$.idEstudiante").value(1))
                .andExpect(jsonPath("$.nombre").value("Manolo"))
                .andExpect(jsonPath("$.curso").exists()) // Verificacion de que el campo curso existe
                .andExpect(jsonPath("$.curso.idCurso").value(1)) // Verificamos que el curso asignado es el correcto
                .andExpect(jsonPath("$.curso.nombreCurso").value("Programación"));
    }

    // NotFound Estudiante
    @Test
    public void testAsignarCursoAEstudianteNotFound() throws Exception {
        when(estudianteService.findById(eq(99L))).thenThrow(new RuntimeException("Estudiante no encontrado")); // Estudiante con ID 99 inexistente

        // Aunque el curso exista, el estudiante no se encontrará
        when(cursoService.findById(any(Integer.class))).thenReturn(curso);

        // Petición PUT a un ID de estudiante que no existe
        mockMvc.perform(put("/api/v1/estudiantes/99/asignarcurso/1"))
                .andExpect(status().isNotFound()); // Esperamos un 404 Not Found
    }

    // NotFound Curso
    @Test
    public void testAsignarCursoAEstudianteCursoNotFound() throws Exception {
        // 1. Mockeamos el escenario donde el estudiante existe pero el curso no
        when(estudianteService.findById(eq(1L))).thenReturn(estudiante); // Estudiante con ID 1
        when(cursoService.findById(eq(99L))).thenThrow(new RuntimeException("Curso no encontrado")); // Curso con ID 99 inexistente

        // 2. Realizamos la petición PUT con un ID de curso que no existe
        mockMvc.perform(put("/api/v1/estudiantes/1/asignarcurso/99"))
                .andExpect(status().isNotFound()); // Esperamos un 404 Not Found
    }




    // Remover curso a estudiante
    @Test
    public void testRemoverEstudianteDeCurso() throws Exception {

        when(estudianteService.findById(eq(1L))).thenReturn(estudiante);
        estudiante.setCurso(null); // Eliminacion de curso a estudiante

        when(estudianteService.save(any(Estudiante.class))).thenReturn(estudiante); // Se guarda el estudiante sin el curso

        // Se realiza la peticion DELETE
        mockMvc.perform(delete("/api/v1/estudiantes/1/removercurso"))
                .andExpect(status().isNoContent()); // Esperamos un 204 No Content

        verify(estudianteService, times(1)).findById(eq(1L));
        verify(estudianteService, times(1)).save(any(Estudiante.class));
    }

    // NotFound Estudiante
    @Test
    public void testRemoverEstudianteDeCursoNotFound() throws Exception {
        when(estudianteService.findById(eq(99L))).thenThrow(new RuntimeException("Estudiante no encontrado"));

        mockMvc.perform(delete("/api/v1/estudiantes/99/removercurso"))
                .andExpect(status().isNotFound()); // Esperamos un 404 Not Found

        verify(estudianteService, never()).save(any(Estudiante.class));
    }
}
