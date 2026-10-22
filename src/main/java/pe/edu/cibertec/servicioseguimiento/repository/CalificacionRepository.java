package pe.edu.cibertec.servicioseguimiento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.servicioseguimiento.entity.Calificacion;

import java.util.List;

public interface CalificacionRepository extends JpaRepository<Calificacion, Integer> {

    List<Calificacion> findByMatriculaIdMatricula(Integer idMatricula);

    List<Calificacion> findByIdTarea(Integer idTarea);

    boolean existsByMatriculaIdMatriculaAndIdTarea(
            Integer idMatricula,
            Integer idTarea
    );
}