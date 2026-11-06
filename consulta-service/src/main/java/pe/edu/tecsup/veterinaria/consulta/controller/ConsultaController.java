package pe.edu.tecsup.veterinaria.consulta.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.tecsup.veterinaria.consulta.dto.ConsultaDetalleResponse;
import pe.edu.tecsup.veterinaria.consulta.dto.ConsultaRequest;
import pe.edu.tecsup.veterinaria.consulta.dto.ConsultaResponse;
import pe.edu.tecsup.veterinaria.consulta.dto.HistorialMascotaResponse;
import pe.edu.tecsup.veterinaria.consulta.services.ConsultaService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService service;

    /** GET /api/consultas  (opcional ?desde=2026-01-01&hasta=2026-12-31) */
    @GetMapping
    public List<ConsultaResponse> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return service.listar(desde, hasta);
    }

    @GetMapping("/{id}")
    public ConsultaResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    /** Consulta + mascota + propietario (comunicacion REST con los otros microservicios). */
    @GetMapping("/{id}/detalle")
    public ConsultaDetalleResponse detalle(@PathVariable Long id) {
        return service.obtenerDetalle(id);
    }

    /** Historial clinico de una mascota con el costo acumulado. */
    @GetMapping("/mascota/{idMascota}")
    public HistorialMascotaResponse historial(@PathVariable Long idMascota) {
        return service.historialPorMascota(idMascota);
    }

    @PostMapping
    public ResponseEntity<ConsultaResponse> registrar(@Valid @RequestBody ConsultaRequest request) {
        ConsultaResponse creada = service.registrar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creada.id()).toUri();
        return ResponseEntity.created(location).body(creada);
    }

    @PutMapping("/{id}")
    public ConsultaResponse actualizar(@PathVariable Long id, @Valid @RequestBody ConsultaRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
