# 📊 User Interaction Statistics Microservice
Microservicio que recibe estadísticas de interacción con usuarios, valida un hash MD5, y si es válido:
- Guarda la estadística en **DynamoDB**.
- Publica el evento en **RabbitMQ**.
---
## 🚀 Tecnologías utilizadas
- Java 17
- Gradle 8.9
- Spring Boot 3 + WebFlux
- Clean Architecture (Scaffold Bancolombia v3.23.1)
- DynamoDB Local
- RabbitMQ
- Docker Compose
- Reactor + Mono/Flux
- Lombok
- Jacoco (para cobertura)
---
## 📦 Estructura del proyecto
```bash
.
├── applications/app-service        # Orquestación del servicio
├── domain/model                   # Entidades y gateways
├── domain/usecase                # Casos de uso
├── infrastructure/entry-points   # Endpoint HTTP con WebFlux
├── infrastructure/driven-adapters
│   ├── dynamo-adapter            # Adapter para DynamoDB
│   └── async-event-bus           # Adapter para RabbitMQ
├── docker-compose.yml
└── README.md
```
## ⚙️ Cómo ejecutar el servicio
1. Clonar el repositorio
```bash
git clone https://github.com/guillermoqp/user_interaction_statistics_2.git
cd user-interaction-statistics
```
2. Levantar servicios necesarios (DynamoDB + RabbitMQ)
```bash
docker-compose up -d
```
#### DynamoDB: http://localhost:8000
#### RabbitMQ UI: http://localhost:15672 (user: guest, pass: guest)
3. Ejecutar el microservicio
```bash
./gradlew bootRun
```
El servicio quedará disponible en:
📍 http://localhost:8080/stats
## 📮 Cómo probar el endpoint
Request válido:
```bash
curl -X POST http://localhost:8080/stats \
-H "Content-Type: application/json" \
-d '{
"totalContactoClientes":250,
"motivoReclamo":25,
"motivoGarantia":10,
"motivoDuda":100,
"motivoCompra":100,
"motivoFelicitaciones":7,
"motivoCambio":8,
"hash":"02946f262f2eb0d8d5c8e76c50433ed8"
}'
```
📥 Respuesta esperada:
```json
{
  "totalContactoClientes":250,
  "motivoReclamo":25,
  "motivoGarantia":10,
  "motivoDuda":100,
  "motivoCompra":100,
  "motivoFelicitaciones":7,
  "motivoCambio":8,
  "hash":"02946f262f2eb0d8d5c8e76c50433ed8",
  "timestamp": 1720012345678
}
```
Request inválido (hash incorrecto):
```json
{
"error": "Hash inválido"
}
```
## 🧪 Ejecutar pruebas y ver cobertura
Ejecutar todas las pruebas
```bash
./gradlew test
```
Generar y ver cobertura (Jacoco)
```bash
./gradlew jacocoTestReport
```
Abre el reporte:
```bash
build/reports/jacoco/test/html/index.html
``` 
## 📬 Contacto
* Autor: [jgquinta-Jose Guillermo Quintanilla Paredes]
* Correo: [jgquinta@bancolombia.com.co]
* GitHub: [https://github.com/guillermoqp]