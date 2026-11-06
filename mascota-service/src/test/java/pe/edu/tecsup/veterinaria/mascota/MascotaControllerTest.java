package pe.edu.tecsup.veterinaria.mascota;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.tecsup.veterinaria.mascota.client.PropietarioClient;
import pe.edu.tecsup.veterinaria.mascota.dto.PropietarioDto;
import pe.edu.tecsup.veterinaria.mascota.exception.ReglaNegocioException;
import pe.edu.tecsup.veterinaria.mascota.exception.ServicioNoDisponibleException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MascotaControllerTest {

    @Autowired
    private MockMvc mvc;

    /** Se simula el microservicio Propietario. */
    @MockitoBean
    private PropietarioClient propietarioClient;

    private static final String NUEVA = """
            {"nombre":"Toby","especie":"Perro","raza":"Beagle","sexo":"MACHO",
             "fechaNacimiento":"2023-03-15","peso":12.5,"idPropietario":1}
            """;

    @Test
    void crudCompleto() throws Exception {
        when(propietarioClient.obtenerPorId(1L))
                .thenReturn(new PropietarioDto(1L, "45871236", "Carlos Alberto", "Ramirez Soto", "987654321", null));

        mvc.perform(post("/api/mascotas").contentType(MediaType.APPLICATION_JSON).content(NUEVA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.fechaNacimiento").value("2023-03-15"));

        mvc.perform(get("/api/mascotas/5/detalle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mascota.nombre").value("Toby"))
                .andExpect(jsonPath("$.propietario.dni").value("45871236"));

        mvc.perform(get("/api/mascotas/propietario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));

        mvc.perform(put("/api/mascotas/5").contentType(MediaType.APPLICATION_JSON)
                        .content(NUEVA.replace("Toby", "Toby Jr")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Toby Jr"));

        mvc.perform(delete("/api/mascotas/5")).andExpect(status().isNoContent());
        mvc.perform(get("/api/mascotas/5")).andExpect(status().isNotFound());
    }

    @Test
    void filtraPorEspecie() throws Exception {
        mvc.perform(get("/api/mascotas").param("especie", "perro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void propietarioInexistenteDevuelve400() throws Exception {
        when(propietarioClient.obtenerPorId(99L))
                .thenThrow(new ReglaNegocioException("El propietario con id 99 no existe"));
        mvc.perform(post("/api/mascotas").contentType(MediaType.APPLICATION_JSON)
                        .content(NUEVA.replace("\"idPropietario\":1", "\"idPropietario\":99")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El propietario con id 99 no existe"));
    }

    @Test
    void servicioPropietarioCaidoDevuelve503() throws Exception {
        when(propietarioClient.obtenerPorId(1L))
                .thenThrow(new ServicioNoDisponibleException("El microservicio Propietario no esta disponible"));
        mvc.perform(post("/api/mascotas").contentType(MediaType.APPLICATION_JSON).content(NUEVA))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    void datosInvalidosDevuelven400() throws Exception {
        mvc.perform(post("/api/mascotas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"\",\"especie\":\"\",\"fechaNacimiento\":\"2999-01-01\",\"peso\":-3}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.nombre").exists())
                .andExpect(jsonPath("$.errores.especie").exists())
                .andExpect(jsonPath("$.errores.fechaNacimiento").value("La fecha de nacimiento no puede ser futura"))
                .andExpect(jsonPath("$.errores.peso").exists())
                .andExpect(jsonPath("$.errores.idPropietario").exists());
    }

    @Test
    void fechaConFormatoIncorrectoDevuelve400() throws Exception {
        mvc.perform(post("/api/mascotas").contentType(MediaType.APPLICATION_JSON)
                        .content(NUEVA.replace("2023-03-15", "15/03/2023")))
                .andExpect(status().isBadRequest());
    }
}
