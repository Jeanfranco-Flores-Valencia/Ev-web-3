package pe.edu.tecsup.veterinaria.mascota.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "mascotas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String nombre;

    @Column(nullable = false, length = 40)
    private String especie;

    @Column(length = 60)
    private String raza;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Sexo sexo;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(precision = 6, scale = 2)
    private BigDecimal peso;

    /** Referencia al propietario: solo el ID, porque vive en otro microservicio/BD. */
    @Column(name = "id_propietario", nullable = false)
    private Long idPropietario;
}
