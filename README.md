sequenceDiagram
    Cliente->>+Controlador: HTTP Request (GET /api/estudiantes)
    Controlador->>+Servicio: Llama a método (ej: getEstudiantes())
    Servicio->>+Repositorio: Invoca consulta (ej: findAll())
    Repositorio->>-BaseDeDatos: Ejecuta SQL
    BaseDeDatos-->>-Repositorio: Datos (ResultSet)
    Repositorio-->>-Servicio: List<Estudiante>
    Servicio-->>-Controlador: List<Estudiante> (DTO opcional)
    Controlador-->>-Cliente: JSON + HTTP Status
