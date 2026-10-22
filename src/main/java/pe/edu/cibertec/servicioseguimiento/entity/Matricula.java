package pe.edu.cibertec.servicioseguimiento.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "matricula",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_alumno_asignacion",
                        columnNames = {"id_alumno", "id_asignacion"}
                )
        }
)
@Getter
@Setter
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_matricula")
    private Integer idMatricula;

    @Column(name = "id_alumno", nullable = false)
    private Integer idAlumno;

    @Column(name = "id_asignacion", nullable = false)
    private Integer idAsignacion;

    @Column(name = "fecha_matricula", nullable = false)
    private LocalDate fechaMatricula;

    @Column(name = "activo", nullable = false)
    private Boolean activo;
}