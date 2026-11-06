package pe.edu.tecsup.veterinaria.mascota.exception;

/** Se lanza cuando se intenta registrar un dato unico repetido (HTTP 409). */
public class RecursoDuplicadoException extends RuntimeException {

    public RecursoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
