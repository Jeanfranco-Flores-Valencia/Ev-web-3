package pe.edu.tecsup.veterinaria.consulta.dto;

/** Consulta junto con los datos de la mascota y su propietario (obtenidos por REST). */
public record ConsultaDetalleResponse(
        ConsultaResponse consulta,
        MascotaDto mascota,
        PropietarioDto propietario
) {
}
