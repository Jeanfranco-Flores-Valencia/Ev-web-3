package pe.edu.tecsup.veterinaria.propietario.services;

import pe.edu.tecsup.veterinaria.propietario.dto.PropietarioRequest;
import pe.edu.tecsup.veterinaria.propietario.dto.PropietarioResponse;

import java.util.List;

public interface PropietarioService {

    List<PropietarioResponse> listar();

    List<PropietarioResponse> buscarPorNombre(String texto);

    PropietarioResponse buscarPorId(Long id);

    PropietarioResponse buscarPorDni(String dni);

    PropietarioResponse registrar(PropietarioRequest request);

    PropietarioResponse actualizar(Long id, PropietarioRequest request);

    void eliminar(Long id);
}
