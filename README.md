# Microservicio JasperReports 📊

Microservicio Spring Boot para generación de reportes PDF usando JasperReports, dockerizado y listo para desplegar en Easypanel.

## 🚀 Características

- ✅ Spring Boot 3.2.1 con Java 17
- ✅ JasperReports 6.21.0 para generación de PDFs
- ✅ Conexión a MySQL
- ✅ API REST con endpoints documentados
- ✅ Docker multi-stage build optimizado
- ✅ Health checks configurados
- ✅ Listo para Easypanel
- ✅ Variables de entorno configurables

## 📋 Requisitos

- Java 17+
- Maven 3.6+
- Docker y Docker Compose (para desarrollo local)
- MySQL 8.0+

## 🏗️ Estructura del Proyecto

```
microservicioJava/
├── src/
│   └── main/
│       ├── java/com/jasper/microservice/
│       │   ├── JasperMicroserviceApplication.java
│       │   ├── controller/
│       │   │   └── ReporteController.java
│       │   ├── service/
│       │   │   └── JasperService.java
│       │   └── dto/
│       │       ├── ReporteRequest.java
│       │       └── ErrorResponse.java
│       └── resources/
│           ├── application.yml
│           └── application-prod.yml
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## 🔧 Configuración

### Variables de Entorno

Crea un archivo `.env` basado en `.env.example`:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=bd
DB_USER=user
DB_PASSWORD=pass
JASPER_TEMPLATE_URL=http://tu-servidor/ContratoClientes.jrxml
```

## 🐳 Desarrollo Local con Docker

### 1. Construir y ejecutar con Docker Compose

```bash
docker-compose up -d
```

Esto iniciará:
- El microservicio en `http://localhost:8080`
- MySQL en `localhost:3306`

### 2. Ver logs

```bash
docker-compose logs -f jasper-microservice
```

### 3. Detener servicios

```bash
docker-compose down
```

## 🛠️ Desarrollo sin Docker

### 1. Compilar el proyecto

```bash
mvn clean install
```

### 2. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

O ejecutar el JAR:

```bash
java -jar target/jasper-microservice-1.0.0.jar
```

## 📡 Endpoints API

### 1. Generar Contrato

**POST** `/api/reportes/contrato`

```json
{
  "parametros": {
    "clienteId": 123,
    "nombre": "Juan Pérez",
    "fechaContrato": "2026-01-21"
  }
}
```

**Respuesta**: PDF binario (application/pdf)

### 2. Generar Reporte Personalizado

**POST** `/api/reportes/personalizado`

```json
{
  "templateNombre": "ReporteVentas",
  "parametros": {
    "fechaInicio": "2026-01-01",
    "fechaFin": "2026-01-31"
  }
}
```

### 3. Health Check

**GET** `/api/reportes/health`

**GET** `/actuator/health`

## 🚢 Despliegue en Easypanel

### Opción 1: Desde Docker Hub

1. **Construir y subir la imagen a Docker Hub**:

```bash
# Construir imagen
docker build -t tu-usuario/jasper-microservice:latest .

# Subir a Docker Hub
docker push tu-usuario/jasper-microservice:latest
```

2. **En Easypanel**:
   - Crear nuevo servicio
   - Seleccionar "Docker Image"
   - Imagen: `tu-usuario/jasper-microservice:latest`
   - Puerto: `8080`

3. **Configurar Variables de Entorno en Easypanel**:
   ```
   SPRING_PROFILES_ACTIVE=prod
   DB_HOST=tu-mysql-host
   DB_PORT=3306
   DB_NAME=bd
   DB_USER=user
   DB_PASSWORD=pass
   JASPER_TEMPLATE_URL=http://tu-servidor/ContratoClientes.jrxml
   JAVA_OPTS=-Xmx512m -Xms256m
   ```

4. **Configurar Health Check**:
   - Path: `/actuator/health`
   - Port: `8080`
   - Interval: `30s`

### Opción 2: Desde GitHub

1. **Subir código a GitHub**

2. **En Easypanel**:
   - Crear nuevo servicio
   - Seleccionar "GitHub Repository"
   - Conectar repositorio
   - Dockerfile path: `/Dockerfile`
   - Configurar variables de entorno (igual que arriba)

### Opción 3: Deploy directo con Dockerfile

1. **En Easypanel**:
   - Crear nuevo servicio
   - Build Method: Dockerfile
   - Pegar contenido del Dockerfile
   - Configurar variables de entorno

## 🗄️ Base de Datos

Asegúrate de tener una base de datos MySQL configurada con:

- Host accesible desde el contenedor
- Usuario y contraseña configurados
- Base de datos creada
- Tablas necesarias para los reportes JasperReports

En Easypanel, puedes:
1. Crear un servicio MySQL desde el marketplace
2. Obtener las credenciales
3. Configurarlas en las variables de entorno del microservicio

## 📊 Monitoreo

El microservicio incluye Spring Boot Actuator con los siguientes endpoints:

- `/actuator/health` - Estado de salud
- `/actuator/info` - Información de la aplicación
- `/actuator/metrics` - Métricas de rendimiento

## 🔒 Seguridad

- El contenedor corre con usuario no-root
- Imagen Alpine Linux (ligera y segura)
- Variables sensibles por variables de entorno
- Health checks configurados

## 🧪 Testing

```bash
# Ejecutar tests
mvn test

# Test de integración
mvn verify
```

## 📝 Ejemplo de Uso con cURL

```bash
# Generar contrato
curl -X POST http://localhost:8080/api/reportes/contrato \
  -H "Content-Type: application/json" \
  -d '{
    "parametros": {
      "clienteId": 123,
      "nombre": "Juan Pérez"
    }
  }' \
  --output contrato.pdf

# Health check
curl http://localhost:8080/actuator/health
```

## 🐛 Troubleshooting

### El contenedor no inicia
- Verifica los logs: `docker logs jasper-microservice`
- Verifica variables de entorno
- Asegúrate que MySQL esté disponible

### Error de conexión a MySQL
- Verifica que `DB_HOST` apunte al host correcto
- En Docker Compose usa el nombre del servicio (`mysql`)
- En Easypanel usa el host interno del servicio MySQL

### Error generando PDF
- Verifica que la URL del template JRXML sea accesible
- Verifica permisos de la base de datos
- Revisa los logs para más detalles

## 📄 Licencia

MIT

## 👨‍💻 Autor

Tu Nombre - [@tu-usuario](https://github.com/tu-usuario)

---

**¿Necesitas ayuda?** Abre un issue en GitHub o contacta al equipo de desarrollo.
