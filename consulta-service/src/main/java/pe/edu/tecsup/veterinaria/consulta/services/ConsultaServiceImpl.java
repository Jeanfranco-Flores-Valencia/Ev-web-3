package pe.edu.tecsup.veterinaria.consulta.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.tecsup.veterinaria.consulta.client.MascotaClient;
import pe.edu.tecsup.veterinaria.consulta.client.PropietarioClient;
import pe.edu.tecsup.veterinaria.consulta.dto.*;
import pe.edu.tecsup.veterinaria.consulta.exception.RecursoNoEncontradoException;
import pe.edu.tecsup.veterinaria.consulta.exception.ReglaNegocioException;
import pe.edu.tecsup.veterinaria.consulta.model.Consulta;
import pe.edu.tecsup.veterinaria.consulta.repository.ConsultaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsultaServiceImpl implements ConsultaService {

    private final ConsultaRepository repository;
    private final MascotaClient mascotaClient;
    private final PropietarioClient propietarioClient;

    @Override
    public List<ConsultaResponse> listar(LocalDate desde, LocalDate hasta) {
        List<Consulta> consultas;
        if (desde == null && hasta == null) {
            consultas = repository.findAll();
        } else {
            LocalDate inicio = desde != null ? desde : LocalDate.of(1900, 1, 1);
            LocalDate fin = hasta != null ? hasta : LocalDate.of(9999, 12, 31);
            if (inicio.isAfter(fin)) {
                throw new ReglaNegocioException("La fecha 'desde' no puede ser posterior a la fecha 'hasta'");
            }
            consultas = repository.findByFechaBetweenOrderByFechaAsc(inicio, fin);
        }
        return consultas.stream().map(ConsultaResponse::from).toList();
    }

    @Override
    public ConsultaResponse buscarPorId(Long id) {
        return ConsultaResponse.from(obtener(id));
    }

    @Override
    public ConsultaDetalleResponse obtenerDetalle(Long id) {
        Consulta consulta = obtener(id);
        MascotaDto mascota = mascotaClient.obtenerPorId(consulta.getIdMascota());
        PropietarioDto propietario = propietarioClient.buscarPorId(mascota.idPropietario()).orElse(null);
        return new ConsultaDetalleResponse(ConsultaResponse.from(consulta), mascota, propietario);
    }

    @Override
    public HistorialMascotaResponse historialPorMascota(Long idMascota) {
        List<ConsultaResponse> consultas = repository.findByIdMascotaOrderByFechaDesc(idMascota)
                .stream().map(ConsultaResponse::from).toList();
        BigDecimal total = consultas.stream().map(ConsultaResponse::costo).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new HistorialMascotaResponse(idMascota, consultas.size(), total, consultas);
    }

    @Override
    @Transactional
    public ConsultaResponse registrar(ConsultaRequest request) {
        // Comunicacion entre microservicios: se valida que la mascota exista
        mascotaClient.obtenerPorId(request.idMascota());
        Consulta consulta = new Consulta();
        copiar(request, consulta);
        return ConsultaResponse.from(repository.save(consulta));
    }

    @Override
    @Transactional
    public ConsultaResponse actualizar(Long id, ConsultaRequest request) {
        Consulta consulta = obtener(id);
        if (!consulta.getIdMascota().equals(request.idMascota())) {
            mascotaClient.obtenerPorId(request.idMascota());
        }
        copiar(request, consulta);
        return ConsultaResponse.from(repository.save(consulta));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.delete(obtener(id));
    }

    private Consulta obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la consulta con id " + id));
    }

    private void copiar(ConsultaRequest r, Consulta c) {
        c.setIdMascota(r.idMascota());
        c.setFecha(r.fecha());
        c.setMotivo(r.motivo().trim());
        c.setDiagnostico(r.diagnostico().trim());
        c.setTratamiento(r.tratamiento());
        c.setVeterinario(r.veterinario());
        c.setCosto(r.costo());
    }
}
