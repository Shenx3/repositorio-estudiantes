package com.estudiantes.estudiantes;

import com.estudiantes.estudiantes.model.Curso;
import com.estudiantes.estudiantes.model.Estudiante;
import com.estudiantes.estudiantes.repository.CursoRepository;
import com.estudiantes.estudiantes.repository.EstudianteRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private CursoRepository cursoRepository;


    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        // Generar cursos
        for (int i = 0; i < 5; i++) {
            Curso curso = new Curso();
            curso.setNombreCurso(faker.educator().course());
            cursoRepository.save(curso);
        }

        List<Curso> cursos = cursoRepository.findAll();

        // Generar estudiantes
        for (int i = 0; i < 50; i++) {
            Estudiante estudiante = new Estudiante();

            // Generar RUT
            String rut = generarRut(faker);
            estudiante.setRunEstudiante(rut);

            estudiante.setNombre(faker.name().firstName());
            estudiante.setApellido(faker.name().lastName());
            estudiante.setEdad(faker.number().numberBetween(17, 40));

            // Asignar curso aleatorio
            estudiante.setCurso(cursos.get(random.nextInt(cursos.size())));

            // Guardar estudiante
            estudianteRepository.save(estudiante);
        }

    }

    private String generarRut(Faker faker) {
        int num = faker.number().numberBetween(1000000, 25000000);
        String dv = calcularDigitoVerificador(num);
        return String.format("%,d-%s", num, dv).replace(",", ".");
    }

    private String calcularDigitoVerificador(int rut) {
        int m = 0, s = 1;
        for (; rut != 0; rut /= 10) {
            s = (s + rut % 10 * (9 - m++ % 6)) % 11;
        }
        return s > 0 ? String.valueOf(s - 1) : "k";
    }
}
