package pe.edu.tecsup.veterinaria.consulta.services;

import pe.edu.tecsup.veterinaria.consulta.dto.ConsultaDetalleResponse;
import pe.edu.tecsup.veterinaria.consulta.dto.ConsultaRequest;
import pe.edu.tecsup.veterinaria.consulta.dto.ConsultaResponse;
import pe.edu.tecsup.veterinaria.consulta.dto.HistorialMascotaResponse;

import java.time.LocalDate;
import java.util.List;

public interface ConsultaService {

    List<ConsultaResponse> listar(LocalDate desde, LocalDate hasta);

    ConsultaResponse buscarPorId(Long id);

    ConsultaDetalleResponse obtenerDetalle(Long id);

    HistorialMascotaResponse historialPorMascota(Long idMascota);

    ConsultaResponse registrar(ConsultaRequest request);

    ConsultaResponse actualizar(Long id, ConsultaRequest request);

    void eliminar(Long id);
}
