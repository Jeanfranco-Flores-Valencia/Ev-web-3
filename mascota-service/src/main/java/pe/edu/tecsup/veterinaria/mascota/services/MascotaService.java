package pe.edu.tecsup.veterinaria.mascota.services;

import pe.edu.tecsup.veterinaria.mascota.dto.MascotaDetalleResponse;
import pe.edu.tecsup.veterinaria.mascota.dto.MascotaRequest;
import pe.edu.tecsup.veterinaria.mascota.dto.MascotaResponse;

import java.util.List;

public interface MascotaService {

    List<MascotaResponse> listar(String especie);

    MascotaResponse buscarPorId(Long id);

    MascotaDetalleResponse obtenerDetalle(Long id);

    List<MascotaResponse> listarPorPropietario(Long idPropietario);

    MascotaResponse registrar(MascotaRequest request);

    MascotaResponse actualizar(Long id, MascotaRequest request);

    void eliminar(Long id);
}
