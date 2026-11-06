package pe.edu.tecsup.veterinaria.propietario.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.tecsup.veterinaria.propietario.dto.PropietarioRequest;
import pe.edu.tecsup.veterinaria.propietario.dto.PropietarioResponse;
import pe.edu.tecsup.veterinaria.propietario.exception.RecursoDuplicadoException;
import pe.edu.tecsup.veterinaria.propietario.exception.RecursoNoEncontradoException;
import pe.edu.tecsup.veterinaria.propietario.model.Propietario;
import pe.edu.tecsup.veterinaria.propietario.repository.PropietarioRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PropietarioServiceImpl implements PropietarioService {

    private final PropietarioRepository repository;

    @Override
    public List<PropietarioResponse> listar() {
        return repository.findAll().stream().map(PropietarioResponse::from).toList();
    }

    @Override
    public List<PropietarioResponse> buscarPorNombre(String texto) {
        return repository.findByApellidosContainingIgnoreCaseOrNombresContainingIgnoreCase(texto, texto)
                .stream().map(PropietarioResponse::from).toList();
    }

    @Override
    public PropietarioResponse buscarPorId(Long id) {
        return PropietarioResponse.from(obtener(id));
    }

    @Override
    public PropietarioResponse buscarPorDni(String dni) {
        return repository.findByDni(dni)
                .map(PropietarioResponse::from)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un propietario con DNI " + dni));
    }

    @Override
    @Transactional
    public PropietarioResponse registrar(PropietarioRequest request) {
        if (repository.existsByDni(request.dni())) {
            throw new RecursoDuplicadoException("Ya existe un propietario registrado con DNI " + request.dni());
        }
        Propietario propietario = new Propietario();
        copiar(request, propietario);
        return PropietarioResponse.from(repository.save(propietario));
    }

    @Override
    @Transactional
    public PropietarioResponse actualizar(Long id, PropietarioRequest request) {
        Propietario propietario = obtener(id);
        if (repository.existsByDniAndIdNot(request.dni(), id)) {
            throw new RecursoDuplicadoException("El DNI " + request.dni() + " pertenece a otro propietario");
        }
        copiar(request, propietario);
        return PropietarioResponse.from(repository.save(propietario));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.delete(obtener(id));
    }

    private Propietario obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el propietario con id " + id));
    }

    private void copiar(PropietarioRequest r, Propietario p) {
        p.setDni(r.dni().trim());
        p.setNombres(r.nombres().trim());
        p.setApellidos(r.apellidos().trim());
        p.setTelefono(r.telefono().trim());
        p.setCorreo(r.correo());
        p.setDireccion(r.direccion());
    }
}
