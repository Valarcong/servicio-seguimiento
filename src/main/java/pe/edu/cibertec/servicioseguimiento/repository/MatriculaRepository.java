package pe.edu.cibertec.servicioseguimiento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.servicioseguimiento.entity.Matricula;

import java.util.List;

public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {

    List<Matricula> findByIdAlumno(Integer idAlumno);

    List<Matricula> findByIdAsignacion(Integer idAsignacion);

    boolean existsByIdAlumnoAndIdAsignacion(Integer idAlumno, Integer idAsignacion);
}