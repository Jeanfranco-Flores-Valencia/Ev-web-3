package pe.edu.tecsup.veterinaria.mascota.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.tecsup.veterinaria.mascota.client.PropietarioClient;
import pe.edu.tecsup.veterinaria.mascota.dto.MascotaDetalleResponse;
import pe.edu.tecsup.veterinaria.mascota.dto.MascotaRequest;
import pe.edu.tecsup.veterinaria.mascota.dto.MascotaResponse;
import pe.edu.tecsup.veterinaria.mascota.exception.RecursoNoEncontradoException;
import pe.edu.tecsup.veterinaria.mascota.model.Mascota;
import pe.edu.tecsup.veterinaria.mascota.repository.MascotaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MascotaServiceImpl implements MascotaService {

    private final MascotaRepository repository;
    private final PropietarioClient propietarioClient;

    @Override
    public List<MascotaResponse> listar(String especie) {
        List<Mascota> mascotas = (especie == null || especie.isBlank())
                ? repository.findAll()
                : repository.findByEspecieIgnoreCase(especie.trim());
        return mascotas.stream().map(MascotaResponse::from).toList();
    }

    @Override
    public MascotaResponse buscarPorId(Long id) {
        return MascotaResponse.from(obtener(id));
    }

    @Override
    public MascotaDetalleResponse obtenerDetalle(Long id) {
        Mascota mascota = obtener(id);
        return new MascotaDetalleResponse(MascotaResponse.from(mascota),
                propietarioClient.obtenerPorId(mascota.getIdPropietario()));
    }

    @Override
    public List<MascotaResponse> listarPorPropietario(Long idPropietario) {
        return repository.findByIdPropietario(idPropietario).stream().map(MascotaResponse::from).toList();
    }

    @Override
    @Transactional
    public MascotaResponse registrar(MascotaRequest request) {
        // Comunicacion entre microservicios: se valida que el propietario exista
        propietarioClient.obtenerPorId(request.idPropietario());
        Mascota mascota = new Mascota();
        copiar(request, mascota);
        return MascotaResponse.from(repository.save(mascota));
    }

    @Override
    @Transactional
    public MascotaResponse actualizar(Long id, MascotaRequest request) {
        Mascota mascota = obtener(id);
        if (!mascota.getIdPropietario().equals(request.idPropietario())) {
            propietarioClient.obtenerPorId(request.idPropietario());
        }
        copiar(request, mascota);
        return MascotaResponse.from(repository.save(mascota));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.delete(obtener(id));
    }

    private Mascota obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la mascota con id " + id));
    }

    private void copiar(MascotaRequest r, Mascota m) {
        m.setNombre(r.nombre().trim());
        m.setEspecie(r.especie().trim());
        m.setRaza(r.raza());
        m.setSexo(r.sexo());
        m.setFechaNacimiento(r.fechaNacimiento());
        m.setPeso(r.peso());
        m.setIdPropietario(r.idPropietario());
    }
}
