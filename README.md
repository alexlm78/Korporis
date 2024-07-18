# Instalacion basica de GestionEmp

## Prerrequisitos
Antes de empezar, asegúrate de tener instalados los siguientes componentes en tu máquina:

1. **Java Development Kit (JDK) 11 o superior**.
2. **Gradle**.
3. **MySQL Server**.

## Pasos de Instalación

### 1. Instalar JDK
Si no tienes el JDK instalado, puedes descargar e instalar el OpenJDK 11 o superior desde [AdoptOpenJDK](https://adoptopenjdk.net/).

### 2. Instalar Gradle
Puedes seguir las instrucciones de la documentación oficial para instalar Gradle: [Installing Gradle](https://gradle.org/install/).

### 3. Instalar y Configurar MySQL
Descarga e instala MySQL Server desde [MySQL Downloads](https://dev.mysql.com/downloads/mysql/).

Después de la instalación:

- Inicia el servicio de MySQL.
- Abre `mysql` en tu terminal o usa MySQL Workbench para crear una base de datos llamada `gestion`.

```sql
CREATE DATABASE gestion;
CREATE USER 'gestion'@'localhost' IDENTIFIED BY 'gestion';
GRANT ALL PRIVILEGES ON gestion.* TO 'gestion'@'localhost';
FLUSH PRIVILEGES;
```

### 4. Preparar tu Proyecto Quarkus

1. **Clona tu repositorio del proyecto o descárgalo en tu máquina local**.

2. **Configura el archivo `application.properties`**.
   Dentro del directorio `src/main/resources/`, asegúrate de tener un archivo `application.properties` con el siguiente contenido:
   
   ```properties
   quarkus.devservices.enabled=false 

   # Datasource for mysql 
   quarkus.datasource.db-kind=mysql 
   quarkus.datasource.username=gestion 
   quarkus.datasource.password=gestion 
   quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/gestion 
   quarkus.hibernate-orm.database.generation=update 
   quarkus.datasource.jdbc.driver=com.mysql.cj.jdbc.Driver
   ```
   
   Modifica los datos de conexión si es necesario, dependiendo de tu configuración local.

3. **Verifica el archivo `build.gradle`**.
   Asegúrate de que tu archivo `build.gradle` tenga las dependencias necesarias para Quarkus y MySQL:
   
   ```groovy
   plugins {
       id 'java'
       id 'io.quarkus'
   }

   repositories {
       mavenCentral()
   }

   dependencies {
       implementation 'io.quarkiverse.jdbc:quarkus-jdbc-sqlite:3.0.7'
       implementation enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}")
       implementation 'io.quarkus:quarkus-hibernate-validator'
       implementation 'io.quarkus:quarkus-resteasy-jsonb'
       implementation 'io.quarkus:quarkus-hibernate-orm-panache'
       implementation 'io.quarkus:quarkus-resteasy'
       implementation 'io.quarkus:quarkus-jdbc-mysql'
       implementation 'io.quarkus:quarkus-arc'
       implementation 'io.quarkus:quarkus-hibernate-orm'
    
       testImplementation 'io.quarkus:quarkus-junit5'
       testImplementation 'io.rest-assured:rest-assured'
   }
   
   group 'dev.kreaker'
   version '0.1.0'
   
   java {
       sourceCompatibility = JavaVersion.VERSION_21
       targetCompatibility = JavaVersion.VERSION_21
   }
   
   test {
       systemProperty "java.util.logging.manager", "org.jboss.logmanager.LogManager"
   }
   
   compileJava {
       options.encoding = 'UTF-8'
       options.compilerArgs << '-parameters'
   }
   ```

### 5. Construir y Ejecutar la Aplicación

1. **Construir la Aplicación**.
   
   Navega hasta la raíz de tu proyecto y ejecuta el siguiente comando para construir tu aplicación:
   
   ```sh
   ./gradlew build
   ```

2. **Ejecutar la Aplicación en Modo Dev**.
   
   Para iniciar tu aplicación en modo desarrollo (dev mode) y así recargar automáticamente los cambios:
   
   ```sh
   ./gradlew quarkusDev
   ```

## Verifica la Instalación
- La aplicación debería estar ejecutándose localmente en `http://localhost:8080`.
- Puedes verificar la conexión a través de la consola de Quarkus y asegurarte de que la aplicación se conecta correctamente a la base de datos MySQL.

## Notas Adicionales
- Asegúrate de que el puerto 3306 (por defecto) esté abierto y accesible para MySQL.
- Verifica los logs de la aplicación para solucionar cualquier problema de configuración o conexión.
- Si utilizas Docker, podrías considerar usar un contenedor Docker para MySQL para simplificar la gestión de la base de datos localmente.

### Quarkus CLI
Puedes obtener la herramienta de CLI de quarkus desde [Quarkus CLI](https://quarkus.io/guides/cli-tooling) con esta puedes tambien ejecutar el aplicativo

   ```sh
   quarkus dev
   ```

## Dudas y comentarios

Cualquier duda o comentario pueden ser enviados a mi correo personal: [alejandro@kreaker.dev](mailto:alejandro@kreaker.dev) o a mi cuenta de GitHub: [alexlm78](https://www.github.com/alexlm78) y con gusto lo revisamos.
