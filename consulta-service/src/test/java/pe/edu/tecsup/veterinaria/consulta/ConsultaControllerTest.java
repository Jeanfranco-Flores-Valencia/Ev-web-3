package pe.edu.tecsup.veterinaria.consulta;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.tecsup.veterinaria.consulta.client.MascotaClient;
import pe.edu.tecsup.veterinaria.consulta.client.PropietarioClient;
import pe.edu.tecsup.veterinaria.consulta.dto.MascotaDto;
import pe.edu.tecsup.veterinaria.consulta.dto.PropietarioDto;
import pe.edu.tecsup.veterinaria.consulta.exception.ReglaNegocioException;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ConsultaControllerTest {

    @Autowired
    private MockMvc mvc;

    /** Se simulan los microservicios Mascota y Propietario. */
    @MockitoBean
    private MascotaClient mascotaClient;

    @MockitoBean
    private PropietarioClient propietarioClient;

    private static final String NUEVA = """
            {"idMascota":1,"fecha":"2026-11-06","motivo":"Vomitos","diagnostico":"Intoxicacion alimentaria",
             "tratamiento":"Suero y dieta","veterinario":"Dra. Ana Lopez","costo":150.00}
            """;

    private static final MascotaDto FIRULAIS =
            new MascotaDto(1L, "Firulais", "Perro", "Labrador", LocalDate.of(2021, 5, 14), 1L);

    @Test
    void crudCompleto() throws Exception {
        when(mascotaClient.obtenerPorId(1L)).thenReturn(FIRULAIS);
        when(propietarioClient.buscarPorId(1L))
                .thenReturn(Optional.of(new PropietarioDto(1L, "45871236", "Carlos Alberto", "Ramirez Soto", "987654321")));

        mvc.perform(post("/api/consultas").contentType(MediaType.APPLICATION_JSON).content(NUEVA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.costo").value(150.00));

        mvc.perform(get("/api/consultas/5/detalle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.consulta.motivo").value("Vomitos"))
                .andExpect(jsonPath("$.mascota.nombre").value("Firulais"))
                .andExpect(jsonPath("$.propietario.apellidos").value("Ramirez Soto"));

        mvc.perform(get("/api/consultas/mascota/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalConsultas").value(3))
                .andExpect(jsonPath("$.costoTotal").value(350.50));

        mvc.perform(put("/api/consultas/5").contentType(MediaType.APPLICATION_JSON)
                        .content(NUEVA.replace("150.00", "0")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.costo").value(0));

        mvc.perform(delete("/api/consultas/5")).andExpect(status().isNoContent());
        mvc.perform(get("/api/consultas/5")).andExpect(status().isNotFound());
    }

    @Test
    void costoNegativoYCamposVaciosDevuelven400() throws Exception {
        mvc.perform(post("/api/consultas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idMascota\":1,\"motivo\":\" \",\"diagnostico\":\"\",\"costo\":-10}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.costo").value("El costo no puede ser negativo"))
                .andExpect(jsonPath("$.errores.motivo").exists())
                .andExpect(jsonPath("$.errores.diagnostico").exists())
                .andExpect(jsonPath("$.errores.fecha").exists());
    }

    @Test
    void mascotaInexistenteDevuelve400() throws Exception {
        when(mascotaClient.obtenerPorId(77L)).thenThrow(new ReglaNegocioException("La mascota con id 77 no existe"));
        mvc.perform(post("/api/consultas").contentType(MediaType.APPLICATION_JSON)
                        .content(NUEVA.replace("\"idMascota\":1", "\"idMascota\":77")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("La mascota con id 77 no existe"));
    }

    @Test
    void filtroPorRangoDeFechas() throws Exception {
        mvc.perform(get("/api/consultas").param("desde", "2026-10-01").param("hasta", "2026-10-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
        mvc.perform(get("/api/consultas").param("desde", "2026-12-01").param("hasta", "2026-01-01"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void idInexistenteEIdNoNumerico() throws Exception {
        mvc.perform(get("/api/consultas/999")).andExpect(status().isNotFound());
        mvc.perform(get("/api/consultas/abc")).andExpect(status().isBadRequest());
    }
}
