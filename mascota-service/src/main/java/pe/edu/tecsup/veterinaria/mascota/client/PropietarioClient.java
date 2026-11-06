package pe.edu.tecsup.veterinaria.mascota.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import pe.edu.tecsup.veterinaria.mascota.dto.PropietarioDto;
import pe.edu.tecsup.veterinaria.mascota.exception.ReglaNegocioException;
import pe.edu.tecsup.veterinaria.mascota.exception.ServicioNoDisponibleException;

/** Cliente REST hacia el microservicio Propietario. */
@Component
public class PropietarioClient {

    private final RestClient restClient;

    public PropietarioClient(RestClient.Builder builder,
                             @Value("${servicios.propietario.url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    /**
     * Obtiene un propietario por ID.
     *
     * @throws ReglaNegocioException         si el propietario no existe (404 en el otro servicio)
     * @throws ServicioNoDisponibleException si el microservicio Propietario no responde
     */
    public PropietarioDto obtenerPorId(Long id) {
        try {
            return restClient.get()
                    .uri("/api/propietarios/{id}", id)
                    .retrieve()
                    .body(PropietarioDto.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ReglaNegocioException("El propietario con id " + id + " no existe");
            }
            throw new ServicioNoDisponibleException("Error al consultar el microservicio Propietario: " + e.getStatusText());
        } catch (RestClientException | IllegalStateException e) {
            throw new ServicioNoDisponibleException(
                    "El microservicio Propietario no esta disponible. Intente nuevamente mas tarde");
        }
    }
}
