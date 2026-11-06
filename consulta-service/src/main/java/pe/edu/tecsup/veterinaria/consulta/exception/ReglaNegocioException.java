package pe.edu.tecsup.veterinaria.consulta.exception;

/** Se lanza cuando no se cumple una regla del negocio (HTTP 400). */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
