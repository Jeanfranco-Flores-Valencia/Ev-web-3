package pe.edu.tecsup.veterinaria.mascota.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.tecsup.veterinaria.mascota.model.Mascota;

import java.util.List;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    List<Mascota> findByIdPropietario(Long idPropietario);

    List<Mascota> findByEspecieIgnoreCase(String especie);
}
