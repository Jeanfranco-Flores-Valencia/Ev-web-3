package pe.edu.tecsup.veterinaria.consulta.dto;

import java.time.LocalDate;

/** Copia local (solo lectura) de los datos que devuelve el microservicio Mascota. */
public record MascotaDto(
        Long id,
        String nombre,
        String especie,
        String raza,
        LocalDate fechaNacimiento,
        Long idPropietario
) {
}
