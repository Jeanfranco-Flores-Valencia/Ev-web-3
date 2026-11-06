package pe.edu.tecsup.veterinaria.consulta.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "consultas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Referencia a la mascota: solo el ID, porque vive en otro microservicio/BD. */
    @Column(name = "id_mascota", nullable = false)
    private Long idMascota;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, length = 200)
    private String motivo;

    @Column(nullable = false, length = 500)
    private String diagnostico;

    @Column(length = 500)
    private String tratamiento;

    @Column(length = 100)
    private String veterinario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal costo;
}
