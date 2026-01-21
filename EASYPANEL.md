# Configuración para Easypanel

Este archivo contiene las instrucciones específicas para desplegar en Easypanel.

## 📋 Checklist Pre-Despliegue

- [ ] Imagen Docker construida y subida a Docker Hub
- [ ] Base de datos MySQL creada en Easypanel
- [ ] Variables de entorno configuradas
- [ ] Health check configurado
- [ ] Dominio configurado (opcional)

## 🚀 Pasos para Desplegar

### 1. Crear Servicio MySQL (si no existe)

1. En Easypanel, ir a "Services" → "Create Service"
2. Seleccionar "MySQL" del marketplace
3. Configurar:
   - Name: `jasper-mysql`
   - Database: `bd`
   - Username: `user`
   - Password: (genera una segura)
4. Crear y esperar a que esté "Running"
5. Copiar el host interno (ejemplo: `jasper-mysql.railway.internal`)

### 2. Crear Servicio del Microservicio

1. En Easypanel, ir a "Services" → "Create Service"
2. Seleccionar "Docker Image"
3. Configurar:

**General:**
- Name: `jasper-microservice`
- Image: `tu-usuario/jasper-microservice:latest`
- Pull Policy: `Always`

**Port Mapping:**
- Container Port: `8080`
- Public: ✅ (si quieres exponerlo públicamente)

**Environment Variables:**
```
SPRING_PROFILES_ACTIVE=prod
DB_HOST=jasper-mysql (o el host de tu MySQL)
DB_PORT=3306
DB_NAME=bd
DB_USER=user
DB_PASSWORD=tu-password-mysql
JASPER_TEMPLATE_URL=http://tu-servidor/ContratoClientes.jrxml
JAVA_OPTS=-Xmx512m -Xms256m
```

**Health Check:**
- Protocol: `HTTP`
- Path: `/actuator/health`
- Port: `8080`
- Initial Delay: `40s`
- Interval: `30s`
- Timeout: `10s`
- Retries: `3`

**Resources (opcional pero recomendado):**
- Memory Limit: `768Mi`
- Memory Request: `512Mi`
- CPU Limit: `1000m`
- CPU Request: `500m`

### 3. Configurar Dominio (Opcional)

1. En el servicio, ir a "Domains"
2. Agregar dominio personalizado o usar el subdominio de Easypanel
3. Configurar SSL (Easypanel lo hace automáticamente)

## 🔗 URLs de Acceso

Después del despliegue:

- **Health Check**: `https://tu-dominio.easypanel.host/actuator/health`
- **API Contrato**: `https://tu-dominio.easypanel.host/api/reportes/contrato`
- **API Personalizado**: `https://tu-dominio.easypanel.host/api/reportes/personalizado`

## 🔄 Actualizar la Aplicación

### Opción 1: Rebuild desde Docker Hub

1. Construir nueva imagen localmente:
   ```bash
   docker build -t tu-usuario/jasper-microservice:latest .
   docker push tu-usuario/jasper-microservice:latest
   ```

2. En Easypanel:
   - Ir al servicio
   - Click en "Redeploy"
   - Seleccionar "Pull Latest Image"

### Opción 2: Auto-deploy desde GitHub

Si configuraste GitHub:
1. Hacer push a la rama principal
2. Easypanel detectará el cambio y reconstruirá automáticamente

## 🔍 Monitoreo

### Ver Logs

1. En Easypanel, ir al servicio
2. Click en "Logs"
3. Ver logs en tiempo real

### Métricas

1. En el servicio, ir a "Metrics"
2. Ver CPU, Memoria, Network
3. Configurar alertas si es necesario

## ⚙️ Variables de Entorno Importantes

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Perfil de Spring Boot | `prod` |
| `DB_HOST` | Host de MySQL | `jasper-mysql` |
| `DB_PORT` | Puerto de MySQL | `3306` |
| `DB_NAME` | Nombre de la BD | `bd` |
| `DB_USER` | Usuario de BD | `user` |
| `DB_PASSWORD` | Password de BD | `secreto123` |
| `JASPER_TEMPLATE_URL` | URL del template JRXML | `http://...` |
| `JAVA_OPTS` | Opciones JVM | `-Xmx512m -Xms256m` |

## 🐛 Debugging en Easypanel

### Servicio no inicia

1. **Ver logs del contenedor**:
   - Services → Tu Servicio → Logs
   - Buscar errores de inicio

2. **Verificar Health Check**:
   - Ir a Health Check settings
   - Aumentar `Initial Delay` a `60s` si es necesario

3. **Verificar conexión a MySQL**:
   ```bash
   # Desde el servicio, usar la consola
   wget -O- http://localhost:8080/actuator/health
   ```

### Error de memoria

Si ves `OutOfMemoryError`:

1. Aumentar límites de memoria en Resources
2. Ajustar `JAVA_OPTS`:
   ```
   JAVA_OPTS=-Xmx1024m -Xms512m
   ```

### Base de datos no conecta

1. Verificar que MySQL esté running
2. Verificar `DB_HOST` - debe ser el nombre interno del servicio
3. Verificar credenciales en variables de entorno
4. Verificar que la red interna permita comunicación

## 📊 Ejemplo de Configuración Completa

```yaml
# Ejemplo de configuración en formato YAML (para referencia)
version: '1'
services:
  jasper-microservice:
    image: tu-usuario/jasper-microservice:latest
    ports:
      - 8080
    environment:
      SPRING_PROFILES_ACTIVE: prod
      DB_HOST: jasper-mysql
      DB_PORT: 3306
      DB_NAME: bd
      DB_USER: user
      DB_PASSWORD: ${MYSQL_PASSWORD}
      JASPER_TEMPLATE_URL: http://templates.example.com/ContratoClientes.jrxml
      JAVA_OPTS: -Xmx512m -Xms256m
    healthcheck:
      path: /actuator/health
      port: 8080
      interval: 30s
      timeout: 10s
      retries: 3
      initial_delay: 40s
    resources:
      limits:
        memory: 768Mi
        cpu: 1000m
      requests:
        memory: 512Mi
        cpu: 500m
```

## 🎯 Siguiente Pasos

1. ✅ Desplegar servicio
2. ✅ Verificar health check
3. ✅ Probar endpoints
4. ✅ Configurar dominio
5. ✅ Configurar alertas
6. ✅ Documentar API endpoints
7. ✅ Implementar CI/CD (opcional)

## 📞 Soporte

Si tienes problemas:
1. Revisa los logs en Easypanel
2. Verifica las variables de entorno
3. Consulta la documentación de Easypanel
4. Abre un issue en el repositorio
