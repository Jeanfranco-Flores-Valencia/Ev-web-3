package pe.edu.tecsup.veterinaria.consulta.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    /**
     * Builder con @LoadBalanced: permite usar el nombre registrado en Eureka
     * (http://mascota-service, http://propietario-service) en lugar de host:puerto.
     */
    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilder() {
        return RestClient.builder();
    }
}
