package pe.edu.tecsup.veterinaria.propietario;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PropietarioControllerTest {

    @Autowired
    private MockMvc mvc;

    private static final String NUEVO = """
            {"dni":"76543210","nombres":"Jeanfranco","apellidos":"Flores Valencia",
             "telefono":"987111222","correo":"jflores@correo.com","direccion":"Arequipa"}
            """;

    @Test
    void listaLosDatosDePrueba() throws Exception {
        mvc.perform(get("/api/propietarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(3)));
    }

    @Test
    void crudCompleto() throws Exception {
        mvc.perform(post("/api/propietarios").contentType(MediaType.APPLICATION_JSON).content(NUEVO))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.dni").value("76543210"));

        mvc.perform(get("/api/propietarios/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombres").value("Jeanfranco"));

        mvc.perform(put("/api/propietarios/4").contentType(MediaType.APPLICATION_JSON)
                        .content(NUEVO.replace("987111222", "999888777")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.telefono").value("999888777"));

        mvc.perform(delete("/api/propietarios/4")).andExpect(status().isNoContent());
        mvc.perform(get("/api/propietarios/4")).andExpect(status().isNotFound());
    }

    @Test
    void idInexistenteDevuelve404() throws Exception {
        mvc.perform(get("/api/propietarios/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("No existe el propietario con id 999"));
    }

    @Test
    void datosInvalidosDevuelven400() throws Exception {
        mvc.perform(post("/api/propietarios").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dni\":\"12\",\"nombres\":\"\",\"telefono\":\"abc\",\"correo\":\"x\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.dni").exists())
                .andExpect(jsonPath("$.errores.nombres").exists())
                .andExpect(jsonPath("$.errores.apellidos").exists())
                .andExpect(jsonPath("$.errores.correo").exists());
    }

    @Test
    void dniDuplicadoDevuelve409() throws Exception {
        mvc.perform(post("/api/propietarios").contentType(MediaType.APPLICATION_JSON)
                        .content(NUEVO.replace("76543210", "45871236")))
                .andExpect(status().isConflict());
    }
}
