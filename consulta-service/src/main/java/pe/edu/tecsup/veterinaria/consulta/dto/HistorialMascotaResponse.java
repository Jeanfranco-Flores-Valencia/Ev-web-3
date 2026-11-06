package pe.edu.tecsup.veterinaria.consulta.dto;

import java.math.BigDecimal;
import java.util.List;

/** Historial clinico de una mascota: sus consultas y el costo acumulado. */
public record HistorialMascotaResponse(
        Long idMascota,
        int totalConsultas,
        BigDecimal costoTotal,
        List<ConsultaResponse> consultas
) {
}
