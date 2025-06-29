package com.estudiantes.estudiantes.service;

import com.estudiantes.estudiantes.model.Curso;
import com.estudiantes.estudiantes.repository.CursoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CursoService {
    @Autowired
    private CursoRepository cursoRepository;

    public List<Curso> findAll(){
        return cursoRepository.findAll();
    }

    public Curso findById(int id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
    }

    public Curso save(Curso curso){
        return cursoRepository.save(curso);
    }

    public void delete(int id){
        cursoRepository.deleteById(id);
    }
}
