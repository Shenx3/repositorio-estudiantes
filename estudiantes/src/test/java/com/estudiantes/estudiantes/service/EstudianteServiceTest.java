package com.estudiantes.estudiantes.service;

import com.estudiantes.estudiantes.model.Estudiante;
import com.estudiantes.estudiantes.repository.EstudianteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // Esta anotación es crucial
public class EstudianteServiceTest {

    @Mock  // Crea un mock del repositorio
    private EstudianteRepository estudianteRepository;

    @InjectMocks  // Inyecta los mocks en el servicio
    private EstudianteService estudianteService;

    private Estudiante estudiante1;
    private Estudiante estudiante2;

    @BeforeEach
    public void setUp() {
        // Configuración inicial de datos de prueba
        estudiante1 = new Estudiante();
        estudiante1.setIdEstudiante(1);
        estudiante1.setRunEstudiante("18.493.402-2");
        estudiante1.setNombre("Manolo");
        estudiante1.setApellido("Lama");
        estudiante1.setEdad(25);

        estudiante2 = new Estudiante();
        estudiante2.setIdEstudiante(2);
        estudiante2.setRunEstudiante("19.876.543-1");
        estudiante2.setNombre("Ana");
        estudiante2.setApellido("Pérez");
        estudiante2.setEdad(22);
    }

    @Test
    public void testFindAllEstudiantes() {
        // 1. Configuración del mock (Arrange)
        List<Estudiante> listaMock = Arrays.asList(estudiante1, estudiante2);
        when(estudianteRepository.findAll()).thenReturn(listaMock);

        // 2. Ejecución del método (Act)
        List<Estudiante> resultado = estudianteService.findAll();

        // 3. Verificaciones (Assert)
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Manolo", resultado.get(0).getNombre());
    }

    @Test
    void testFindEstudianteById() {
        // Arrange
        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante1));

        // Act
        Estudiante result = estudianteService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Manolo", result.getNombre());
    }

    @Test
    void testSaveEstudiante() {
        // Arrange
        when(estudianteRepository.save(any(Estudiante.class))).thenReturn(estudiante1);

        // Act
        Estudiante result = estudianteService.save(estudiante1);

        // Assert
        assertNotNull(result);
        assertEquals("Lama", result.getApellido());
    }

    @Test
    void testDeleteEstudiante() {
        // Arrange
        doNothing().when(estudianteRepository).deleteById(1L);

        // Act
        estudianteService.delete(1L);

        // Assert
        verify(estudianteRepository, times(1)).deleteById(1L);
    }

}