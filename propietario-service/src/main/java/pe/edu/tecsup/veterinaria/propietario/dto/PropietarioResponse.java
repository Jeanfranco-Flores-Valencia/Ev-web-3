package pe.edu.tecsup.veterinaria.propietario.dto;

import pe.edu.tecsup.veterinaria.propietario.model.Propietario;

import java.time.LocalDateTime;

/** Datos que se devuelven al cliente (no se expone la entidad JPA directamente). */
public record PropietarioResponse(
        Long id,
        String dni,
        String nombres,
        String apellidos,
        String telefono,
        String correo,
        String direccion,
        LocalDateTime fechaRegistro
) {
    public static PropietarioResponse from(Propietario p) {
        return new PropietarioResponse(p.getId(), p.getDni(), p.getNombres(), p.getApellidos(),
                p.getTelefono(), p.getCorreo(), p.getDireccion(), p.getFechaRegistro());
    }
}
