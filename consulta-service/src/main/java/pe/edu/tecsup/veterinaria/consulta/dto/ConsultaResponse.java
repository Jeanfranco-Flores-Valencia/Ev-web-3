package pe.edu.tecsup.veterinaria.consulta.dto;

import pe.edu.tecsup.veterinaria.consulta.model.Consulta;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConsultaResponse(
        Long id,
        Long idMascota,
        LocalDate fecha,
        String motivo,
        String diagnostico,
        String tratamiento,
        String veterinario,
        BigDecimal costo
) {
    public static ConsultaResponse from(Consulta c) {
        return new ConsultaResponse(c.getId(), c.getIdMascota(), c.getFecha(), c.getMotivo(),
                c.getDiagnostico(), c.getTratamiento(), c.getVeterinario(), c.getCosto());
    }
}
