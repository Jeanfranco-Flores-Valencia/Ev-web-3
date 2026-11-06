package pe.edu.tecsup.veterinaria.mascota.dto;

import pe.edu.tecsup.veterinaria.mascota.model.Mascota;
import pe.edu.tecsup.veterinaria.mascota.model.Sexo;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MascotaResponse(
        Long id,
        String nombre,
        String especie,
        String raza,
        Sexo sexo,
        LocalDate fechaNacimiento,
        BigDecimal peso,
        Long idPropietario
) {
    public static MascotaResponse from(Mascota m) {
        return new MascotaResponse(m.getId(), m.getNombre(), m.getEspecie(), m.getRaza(), m.getSexo(),
                m.getFechaNacimiento(), m.getPeso(), m.getIdPropietario());
    }
}
