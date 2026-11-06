package pe.edu.tecsup.veterinaria.consulta.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Datos de entrada para registrar o actualizar una consulta. */
public record ConsultaRequest(

        @NotNull(message = "El idMascota es obligatorio")
        @Positive(message = "El idMascota debe ser un numero positivo")
        Long idMascota,

        @NotNull(message = "La fecha es obligatoria (formato yyyy-MM-dd)")
        LocalDate fecha,

        @NotBlank(message = "El motivo es obligatorio")
        @Size(max = 200, message = "El motivo no debe superar 200 caracteres")
        String motivo,

        @NotBlank(message = "El diagnostico es obligatorio")
        @Size(max = 500, message = "El diagnostico no debe superar 500 caracteres")
        String diagnostico,

        @Size(max = 500, message = "El tratamiento no debe superar 500 caracteres")
        String tratamiento,

        @Size(max = 100, message = "El nombre del veterinario no debe superar 100 caracteres")
        String veterinario,

        @NotNull(message = "El costo es obligatorio")
        @DecimalMin(value = "0.00", message = "El costo no puede ser negativo")
        @Digits(integer = 8, fraction = 2, message = "El costo admite como maximo 8 enteros y 2 decimales")
        BigDecimal costo
) {
}
