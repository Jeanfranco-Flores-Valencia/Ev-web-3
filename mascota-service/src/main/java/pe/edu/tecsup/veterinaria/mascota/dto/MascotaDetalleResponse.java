package pe.edu.tecsup.veterinaria.mascota.dto;

/** Mascota junto con los datos de su propietario (obtenidos por REST). */
public record MascotaDetalleResponse(
        MascotaResponse mascota,
        PropietarioDto propietario
) {
}
