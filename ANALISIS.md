# Documento Técnico: Sistema de Asistencia de Empleados

## Introducción

La empresa ha solicitado agregar la funcionalidad de manejo de asistencia de los empleados. Los empleados pueden trabajar en distintos centros de trabajo según una planificación mensual, y su asistencia se corrobora diariamente mediante llamadas telefónicas a los encargados de cada centro de trabajo.

## Suposiciones

1. Cada empleado puede estar asignado a un centro de trabajo diferente cada día.
2. Los encargados de cada centro de trabajo corroboran la asistencia de los empleados diariamente mediante llamadas telefónicas.
3. Se requiere una interfaz de usuario para gestionar la planificación mensual, registrar la asistencia diaria y generar reportes de asistencia.

# Consultas al Encargado del Proceso

1. ¿Cómo se asignan los empleados a los centros de trabajo? ¿Existe algún sistema automatizado o es un proceso manual?
2. ¿Qué información se requiere registrar para cada entrada de asistencia (e.g., hora de llegada, hora de salida, observaciones)?
3. ¿Hay algún requerimiento específico para los reportes de asistencia (e.g., formato, frecuencia)?
4. ¿Se necesita una funcionalidad para gestionar los centros de trabajo y los encargados de cada centro?
5. ¿Existen roles y permisos específicos para los usuarios que gestionarán esta funcionalidad?

## Diseño de la Base de Datos

### Tablas

#### Empleados
```dbml
Table Empleados {
    id bigint [pk, increment]
    nombre varchar(100)
    apellido varchar(100)
    telefono varchar(15)
    email varchar(100)
    puesto varchar(50)
}
```

#### Centros de Trabajo
```dbml

Table CentrosTrabajo {
    id bigint [pk, increment]
    nombre varchar(100)
    direccion varchar(255)
    encargado varchar(100)
}
```

#### Planificación
```dbml
Table Planificacion {
    id bigint [pk, increment]
    empleado_id bigint [ref: > Empleados.id]
    centro_trabajo_id bigint [ref: > CentrosTrabajo.id]
    fecha date
}
```

#### Asistencia
```dbml
Table Asistencia {
    id bigint [pk, increment]
    planificacion_id bigint [ref: > PlanificacionMensual.id]
    fecha date
    hora_llegada time
    hora_salida time
    observaciones text
}
```

## Interfaz de Usuario

### Pantallas de Operación

#### Pantalla de Gestión de Empleados

* Listado de empleados.
* Formulario para agregar/editar empleados.

#### Pantalla de Gestión de Centros de Trabajo

* Listado de centros de trabajo.
* Formulario para agregar/editar centros de trabajo.
* Asignación de encargados a los centros de trabajo.

#### Pantalla de Planificación Mensual

* Calendario mensual con asignaciones de empleados a centros de trabajo.
* Formulario para agregar/editar asignaciones.

#### Pantalla de Registro de Asistencia Diaria

* Listado diario de empleados asignados a cada centro de trabajo.
* Formulario para registrar hora de llegada, hora de salida y observaciones.

#### Pantalla de Reportes de Asistencia

* Generación de reportes de asistencia por empleado, centro de trabajo y/o rango de fechas.

## Procesos

### Asignación de Empleados a Centros de Trabajo

* Proceso para crear una planificación mensual donde se asignan empleados a distintos centros de trabajo para cada día del mes.

### Registro de Asistencia Diaria

* Proceso donde los encargados registran la asistencia diaria de los empleados mediante un formulario que incluye la hora de llegada, hora de salida y observaciones.

### Generación de Reportes de Asistencia

* Proceso para generar reportes de asistencia que pueden ser filtrados por empleado, centro de trabajo y/o rango de fechas.

# Diagrama 

## Diagrama ER

```dbml
Table Empleados {
    id bigint [pk, increment]
    nombre varchar(100)
    apellido varchar(100)
    telefono varchar(15)
    email varchar(100)
    puesto varchar(50)
}

Table CentrosTrabajo {
    id bigint [pk, increment]
    nombre varchar(100)
    direccion varchar(255)
    encargado_id bigint [ref: > Empleados.id]
}

Table PlanificacionMensual {
    id bigint [pk, increment]
    empleado_id bigint [ref: > Empleados.id]
    centro_trabajo_id bigint [ref: > CentrosTrabajo.id]
    fecha date
}

Table AsistenciaDiaria {
    id bigint [pk, increment]
    planificacion_id bigint [ref: > PlanificacionMensual.id]
    fecha date
    hora_llegada time
    hora_salida time
    observaciones text
}
```

# Recomendaciones

1. Utilizar un framework como Quarkus para la implementación de los servicios backend, asegurando un rendimiento óptimo y una fácil escalabilidad.
2. Implementar una interfaz de usuario moderna utilizando un framework como Angular o React.
3. Asegurar la integridad de los datos mediante validaciones tanto en el frontend como en el backend.
4. Considerar la implementación de roles y permisos para gestionar el acceso a las diferentes funcionalidades de la aplicación.

