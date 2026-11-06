package pe.edu.tecsup.veterinaria.consulta.exception;

/** Se lanza cuando otro microservicio no responde (HTTP 503). */
public class ServicioNoDisponibleException extends RuntimeException {

    public ServicioNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
