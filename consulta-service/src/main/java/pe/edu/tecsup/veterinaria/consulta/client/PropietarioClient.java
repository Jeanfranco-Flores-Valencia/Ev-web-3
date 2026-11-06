package pe.edu.tecsup.veterinaria.consulta.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import pe.edu.tecsup.veterinaria.consulta.dto.PropietarioDto;

import java.util.Optional;

/** Cliente REST hacia el microservicio Propietario (solo para mostrar el detalle). */
@Component
public class PropietarioClient {

    private final RestClient restClient;

    public PropietarioClient(RestClient.Builder builder, @Value("${servicios.propietario.url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    /** Devuelve vacio si el propietario no existe o el servicio no responde (el detalle es informativo). */
    public Optional<PropietarioDto> buscarPorId(Long id) {
        try {
            return Optional.ofNullable(restClient.get()
                    .uri("/api/propietarios/{id}", id)
                    .retrieve()
                    .body(PropietarioDto.class));
        } catch (RestClientException | IllegalStateException e) {
            return Optional.empty();
        }
    }
}
