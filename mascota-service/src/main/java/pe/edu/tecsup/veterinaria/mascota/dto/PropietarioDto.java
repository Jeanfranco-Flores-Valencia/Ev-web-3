package pe.edu.tecsup.veterinaria.mascota.dto;

/** Copia local (solo lectura) de los datos que devuelve el microservicio Propietario. */
public record PropietarioDto(
        Long id,
        String dni,
        String nombres,
        String apellidos,
        String telefono,
        String correo
) {
}
