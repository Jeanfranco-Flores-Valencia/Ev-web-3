package pe.edu.tecsup.veterinaria.consulta.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import pe.edu.tecsup.veterinaria.consulta.dto.MascotaDto;
import pe.edu.tecsup.veterinaria.consulta.exception.ReglaNegocioException;
import pe.edu.tecsup.veterinaria.consulta.exception.ServicioNoDisponibleException;

/** Cliente REST hacia el microservicio Mascota. */
@Component
public class MascotaClient {

    private final RestClient restClient;

    public MascotaClient(RestClient.Builder builder, @Value("${servicios.mascota.url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    /**
     * @throws ReglaNegocioException         si la mascota no existe
     * @throws ServicioNoDisponibleException si el microservicio Mascota no responde
     */
    public MascotaDto obtenerPorId(Long id) {
        try {
            return restClient.get()
                    .uri("/api/mascotas/{id}", id)
                    .retrieve()
                    .body(MascotaDto.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ReglaNegocioException("La mascota con id " + id + " no existe");
            }
            throw new ServicioNoDisponibleException("Error al consultar el microservicio Mascota: " + e.getStatusText());
        } catch (RestClientException | IllegalStateException e) {
            throw new ServicioNoDisponibleException(
                    "El microservicio Mascota no esta disponible. Intente nuevamente mas tarde");
        }
    }
}
