package pe.edu.tecsup.veterinaria.mascota.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.tecsup.veterinaria.mascota.dto.MascotaDetalleResponse;
import pe.edu.tecsup.veterinaria.mascota.dto.MascotaRequest;
import pe.edu.tecsup.veterinaria.mascota.dto.MascotaResponse;
import pe.edu.tecsup.veterinaria.mascota.services.MascotaService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
@RequiredArgsConstructor
public class MascotaController {

    private final MascotaService service;

    /** GET /api/mascotas  (opcional ?especie=Perro) */
    @GetMapping
    public List<MascotaResponse> listar(@RequestParam(required = false) String especie) {
        return service.listar(especie);
    }

    @GetMapping("/{id}")
    public MascotaResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    /** Mascota + datos del propietario obtenidos del microservicio Propietario. */
    @GetMapping("/{id}/detalle")
    public MascotaDetalleResponse detalle(@PathVariable Long id) {
        return service.obtenerDetalle(id);
    }

    @GetMapping("/propietario/{idPropietario}")
    public List<MascotaResponse> listarPorPropietario(@PathVariable Long idPropietario) {
        return service.listarPorPropietario(idPropietario);
    }

    @PostMapping
    public ResponseEntity<MascotaResponse> registrar(@Valid @RequestBody MascotaRequest request) {
        MascotaResponse creada = service.registrar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creada.id()).toUri();
        return ResponseEntity.created(location).body(creada);
    }

    @PutMapping("/{id}")
    public MascotaResponse actualizar(@PathVariable Long id, @Valid @RequestBody MascotaRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
