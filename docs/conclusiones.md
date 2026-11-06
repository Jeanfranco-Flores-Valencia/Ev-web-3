# Conclusiones – Jeanfranco Anderson Flores Valencia

## Cinco aprendizajes técnicos

1. **Separar por responsabilidad y por datos.** Propietario, Mascota y Consulta tienen ciclos de vida
   distintos. Al darle a cada uno su propio proyecto, puerto y base de datos (`bd_propietario`,
   `bd_mascota`, `bd_consulta`), un cambio o una caída en uno no obliga a modificar ni detener los
   demás. El costo es que ya no existen claves foráneas entre tablas: las relaciones se guardan como
   IDs (`idPropietario`, `idMascota`) y se validan por REST.

2. **Eureka + API Gateway desacoplan las direcciones.** Ningún servicio conoce el host y puerto del
   otro. Mascota llama a `http://propietario-service` y un `RestClient.Builder` con `@LoadBalanced`
   resuelve la instancia real en Eureka. El Gateway hace lo mismo con `lb://` y expone un único punto
   de entrada (`:8080`) para el cliente.

3. **Los perfiles YAML evitan cambiar código entre ambientes.** El mismo JAR funciona con MySQL local
   y datos de prueba (`dev`), con H2 en memoria para pruebas automáticas sin MySQL ni Eureka (`test`)
   y con credenciales por variables de entorno, `ddl-auto: validate` y logs reducidos (`prod`).

4. **Los DTO con Bean Validation protegen la entidad y la API.** Los `record` de request
   (`@NotBlank`, `@DecimalMin("0.00")`, `@PastOrPresent`, `@Pattern`) rechazan los datos inválidos antes
   de llegar al servicio, y los de response evitan exponer la entidad JPA. Spring Data JPA genera las
   consultas a partir del nombre del método (`findByIdMascotaOrderByFechaDesc`, `existsByDniAndIdNot`).

5. **Un manejo de errores centralizado da respuestas predecibles.** Con `@RestControllerAdvice` cada
   excepción se convierte en un código HTTP coherente (400, 404, 409, 503, 500) y en el mismo JSON
   (`mensaje`, `errores` por campo, `path`). Así el cliente sabe qué corregir y no se filtran trazas
   internas.

## Dificultad encontrada y solución

**Problema:** los datos de prueba se insertan con IDs fijos (1, 2, 3…) para que las referencias entre
bases sean conocidas (la mascota 1 pertenece al propietario 1). En el perfil `test` con H2, el primer
`POST` fallaba con *clave primaria duplicada*: H2 no adelanta el contador de `IDENTITY` cuando se
insertan IDs manualmente, así que intentaba generar otra vez el id 1. Además, en MySQL el script se
volvía a ejecutar en cada reinicio del perfil `dev` y podía duplicar registros.

**Solución:** se separaron los scripts por plataforma con `spring.sql.init.platform`:
`data-mysql.sql` usa `INSERT IGNORE`, así que al reiniciar no se duplica nada, y `data-h2.sql` termina
con `ALTER TABLE ... ALTER COLUMN id RESTART WITH n` para continuar el contador después de los datos de
prueba. Las pruebas automáticas (`mvn package`) verifican que el siguiente registro reciba el ID correcto.

*(Otra dificultad menor: con `serverTimezone=America/Lima` en la URL JDBC y la JVM en otra zona,
`fechaRegistro` se mostraba con 5 horas de diferencia. Se quitó el parámetro para que el driver use la
zona local de la JVM.)*

## Guía para la sustentación

**¿Por qué separó el sistema en estos servicios?** Cada entidad del negocio es una capacidad distinta:
la ficha del cliente, la ficha del paciente animal y el acto médico. Cada una se puede desplegar,
escalar y mantener por separado, con su propia BD. Consulta es la que más crece (muchas por mascota) y
se puede escalar sola.

**¿Cómo persiste los datos?** Con Spring Data JPA y Hibernate. La entidad (`@Entity Consulta`) se
mapea a la tabla `consultas` de `bd_consulta`, el `ConsultaRepository extends JpaRepository` hace el
CRUD, y el servicio (`@Transactional`) aplica las reglas antes de `save()`.

**¿Qué ocurre si la API recibe información inválida?** `@Valid` dispara Bean Validation sobre el DTO.
Si falla, se lanza `MethodArgumentNotValidException`, que el `GlobalExceptionHandler` convierte en
**400** con un mensaje por campo (ej. `"costo": "El costo no puede ser negativo"`). Si el ID no existe
→ **404**; si la mascota o el propietario referenciado no existe en el otro servicio → **400**; si ese
servicio está caído → **503**.

**Cambios en vivo que el docente podría pedir (y dónde se hacen):**

| Pedido | Dónde |
|---|---|
| Nuevo campo (ej. `color` en Mascota) | `model/Mascota` + `dto/MascotaRequest` + `dto/MascotaResponse` + `copiar()` en el servicio |
| Nueva validación (ej. costo máximo) | anotación en `dto/ConsultaRequest` (`@DecimalMax`) |
| Nueva búsqueda (ej. por veterinario) | método derivado en `ConsultaRepository` + endpoint en `ConsultaController` |
| Cambiar puerto o BD | `server.port` / `spring.datasource.url` en `application.yml` |
| Nueva ruta en el Gateway | `spring.cloud.gateway.server.webflux.routes` en `api-gateway/application.yml` |
