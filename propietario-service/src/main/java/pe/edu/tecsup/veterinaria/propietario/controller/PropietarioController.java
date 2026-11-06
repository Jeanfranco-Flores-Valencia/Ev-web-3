package pe.edu.tecsup.veterinaria.propietario.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.tecsup.veterinaria.propietario.dto.PropietarioRequest;
import pe.edu.tecsup.veterinaria.propietario.dto.PropietarioResponse;
import pe.edu.tecsup.veterinaria.propietario.services.PropietarioService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/propietarios")
@RequiredArgsConstructor
public class PropietarioController {

    private final PropietarioService service;

    /** GET /api/propietarios  (opcional ?nombre=texto para filtrar) */
    @GetMapping
    public List<PropietarioResponse> listar(@RequestParam(required = false) String nombre) {
        return (nombre == null || nombre.isBlank()) ? service.listar() : service.buscarPorNombre(nombre);
    }

    @GetMapping("/{id}")
    public PropietarioResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/dni/{dni}")
    public PropietarioResponse buscarPorDni(@PathVariable String dni) {
        return service.buscarPorDni(dni);
    }

    @PostMapping
    public ResponseEntity<PropietarioResponse> registrar(@Valid @RequestBody PropietarioRequest request) {
        PropietarioResponse creado = service.registrar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.id()).toUri();
        return ResponseEntity.created(location).body(creado);
    }

    @PutMapping("/{id}")
    public PropietarioResponse actualizar(@PathVariable Long id, @Valid @RequestBody PropietarioRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
