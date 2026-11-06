package pe.edu.tecsup.veterinaria.mascota.dto;

import jakarta.validation.constraints.*;
import pe.edu.tecsup.veterinaria.mascota.model.Sexo;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Datos de entrada para registrar o actualizar una mascota. */
public record MascotaRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 60, message = "El nombre no debe superar 60 caracteres")
        String nombre,

        @NotBlank(message = "La especie es obligatoria")
        @Size(max = 40, message = "La especie no debe superar 40 caracteres")
        String especie,

        @Size(max = 60, message = "La raza no debe superar 60 caracteres")
        String raza,

        Sexo sexo,

        @NotNull(message = "La fecha de nacimiento es obligatoria (formato yyyy-MM-dd)")
        @PastOrPresent(message = "La fecha de nacimiento no puede ser futura")
        LocalDate fechaNacimiento,

        @Positive(message = "El peso debe ser mayor que cero")
        @Digits(integer = 4, fraction = 2, message = "El peso admite como maximo 4 enteros y 2 decimales")
        BigDecimal peso,

        @NotNull(message = "El idPropietario es obligatorio")
        @Positive(message = "El idPropietario debe ser un numero positivo")
        Long idPropietario
) {
}
