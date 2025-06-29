package com.estudiantes.estudiantes.service;

import com.estudiantes.estudiantes.model.Curso;
import com.estudiantes.estudiantes.repository.CursoRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoService cursoService;

    private Curso curso;

    @BeforeEach
    void setUp() {
        curso = new Curso();
        curso.setIdCurso(1);
        curso.setNombreCurso("Programación");
    }

    @Test
    void testFindAllCursos() {
        // Arrange
        when(cursoRepository.findAll()).thenReturn(Arrays.asList(curso));

        // Act
        List<Curso> result = cursoService.findAll();

        // Assert
        assertEquals(1, result.size());
        verify(cursoRepository, times(1)).findAll();
    }

    @Test
    void testFindCursoById() {
        // Arrange
        when(cursoRepository.findById(1)).thenReturn(Optional.of(curso));

        // Act
        Curso result = cursoService.findById(1);

        // Assert
        assertNotNull(result);
        assertEquals("Programación", result.getNombreCurso());
    }

    @Test
    void testSaveCurso() {
        // Arrange
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        // Act
        Curso result = cursoService.save(curso);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getIdCurso());
    }

    @Test
    void testDeleteCurso() {
        // Arrange
        doNothing().when(cursoRepository).deleteById(1);

        // Act
        cursoService.delete(1);

        // Assert
        verify(cursoRepository, times(1)).deleteById(1);
    }
}
