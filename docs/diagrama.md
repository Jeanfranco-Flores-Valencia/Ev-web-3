# Diagramas – Sistema de Atención Veterinaria

## 1. Microservicios, responsabilidades y bases de datos

```mermaid
flowchart TB
    subgraph Cliente
        PM[Postman / Front-end]
    end
    subgraph Infraestructura
        GW["API Gateway :8080<br/>enruta /api/** con lb://"]
        EU["Eureka Server :8761<br/>registro y descubrimiento"]
    end
    subgraph Negocio
        P["propietario-service :8081<br/>CRUD propietarios<br/>DNI único"]
        M["mascota-service :8082<br/>CRUD mascotas<br/>valida idPropietario"]
        C["consulta-service :8083<br/>CRUD consultas<br/>valida idMascota, costo ≥ 0<br/>historial por mascota"]
    end
    DBP[("MySQL<br/>bd_propietario<br/>tabla propietarios")]
    DBM[("MySQL<br/>bd_mascota<br/>tabla mascotas")]
    DBC[("MySQL<br/>bd_consulta<br/>tabla consultas")]

    PM --> GW
    GW --> P & M & C
    M -- "GET /api/propietarios/{id}" --> P
    C -- "GET /api/mascotas/{id}" --> M
    C -- "GET /api/propietarios/{id}" --> P
    P --> DBP
    M --> DBM
    C --> DBC
    GW & P & M & C -.registro.-> EU
```

## 2. Modelo de datos (una base por microservicio)

```mermaid
erDiagram
    PROPIETARIOS {
        bigint id PK
        varchar dni UK "8 dígitos"
        varchar nombres
        varchar apellidos
        varchar telefono
        varchar correo
        varchar direccion
        datetime fecha_registro
    }
    MASCOTAS {
        bigint id PK
        varchar nombre
        varchar especie
        varchar raza
        enum sexo "MACHO | HEMBRA"
        date fecha_nacimiento
        decimal peso
        bigint id_propietario "ID lógico (otra BD)"
    }
    CONSULTAS {
        bigint id PK
        bigint id_mascota "ID lógico (otra BD)"
        date fecha
        varchar motivo
        varchar diagnostico
        varchar tratamiento
        varchar veterinario
        decimal costo "costo >= 0"
    }
    PROPIETARIOS ||..o{ MASCOTAS : "idPropietario (validado por REST)"
    MASCOTAS ||..o{ CONSULTAS : "idMascota (validado por REST)"
```

Las líneas punteadas indican que **no hay clave foránea física**: cada tabla está en una base de datos
distinta y la integridad se valida llamando al otro microservicio.

## 3. Secuencia: registrar una consulta

```mermaid
sequenceDiagram
    actor U as Postman
    participant G as API Gateway
    participant E as Eureka
    participant C as consulta-service
    participant M as mascota-service
    participant DB as bd_consulta

    U->>G: POST /api/consultas {idMascota: 5, costo: 70, ...}
    G->>E: ¿instancias de consulta-service?
    G->>C: reenvía la petición
    C->>C: @Valid (campos obligatorios, costo >= 0)
    alt datos inválidos
        C-->>U: 400 + errores por campo
    end
    C->>M: GET /api/mascotas/5 (RestClient @LoadBalanced)
    alt mascota no existe
        M-->>C: 404
        C-->>U: 400 "La mascota con id 5 no existe"
    else mascota-service caído
        C-->>U: 503 "El microservicio Mascota no esta disponible"
    end
    M-->>C: 200 {mascota}
    C->>DB: INSERT INTO consultas
    C-->>U: 201 Created + Location
```
