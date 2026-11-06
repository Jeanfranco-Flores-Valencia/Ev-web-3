package pe.edu.tecsup.veterinaria.propietario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.tecsup.veterinaria.propietario.model.Propietario;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropietarioRepository extends JpaRepository<Propietario, Long> {

    Optional<Propietario> findByDni(String dni);

    boolean existsByDni(String dni);

    boolean existsByDniAndIdNot(String dni, Long id);

    List<Propietario> findByApellidosContainingIgnoreCaseOrNombresContainingIgnoreCase(String apellidos, String nombres);
}
