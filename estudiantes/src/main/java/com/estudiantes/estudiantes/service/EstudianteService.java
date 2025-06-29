package com.estudiantes.estudiantes.service;

import com.estudiantes.estudiantes.model.Estudiante;
import com.estudiantes.estudiantes.repository.EstudianteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EstudianteService {
    @Autowired // Inyeccion de dependencia del repositorio
    private EstudianteRepository estudianteRepository;

    public List<Estudiante> findAll(){ // Listar estudiantes
        return estudianteRepository.findAll();
    }

    public Estudiante findById(int id) { // Encontrar estudiante por ID
        return estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
    }

    public Estudiante save(Estudiante estudiante) { // Guardar estudiante
        return estudianteRepository.save(estudiante);
    }

    public void delete(int id){ // Eliminar estudiante
        estudianteRepository.deleteById(id);
    }
}
