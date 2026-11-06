package pe.edu.tecsup.veterinaria.consulta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.tecsup.veterinaria.consulta.model.Consulta;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findByIdMascotaOrderByFechaDesc(Long idMascota);

    List<Consulta> findByFechaBetweenOrderByFechaAsc(LocalDate desde, LocalDate hasta);
}
