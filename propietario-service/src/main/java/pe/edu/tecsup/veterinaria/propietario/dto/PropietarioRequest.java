package pe.edu.tecsup.veterinaria.propietario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Datos de entrada para registrar o actualizar un propietario. */
public record PropietarioRequest(

        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 digitos")
        String dni,

        @NotBlank(message = "Los nombres son obligatorios")
        @Size(max = 80, message = "Los nombres no deben superar 80 caracteres")
        String nombres,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(max = 80, message = "Los apellidos no deben superar 80 caracteres")
        String apellidos,

        @NotBlank(message = "El telefono es obligatorio")
        @Pattern(regexp = "\\d{7,15}", message = "El telefono debe tener entre 7 y 15 digitos")
        String telefono,

        @Email(message = "El correo no tiene un formato valido")
        @Size(max = 100, message = "El correo no debe superar 100 caracteres")
        String correo,

        @Size(max = 150, message = "La direccion no debe superar 150 caracteres")
        String direccion
) {
}
