#  MediCore Server - API Centralizada

Backend robusto para la gestión hospitalaria nacional de MediCore. Este proyecto es el núcleo del ecosistema que conecta la aplicación de escritorio (Admin) y la aplicación web (Pacientes).

##  Requisitos Previos
* **Java:** 17 (LTS)
* **Maven:** 3.9+ (o usar el `./mvnw` incluido)
* **IDE:** VS Code, IntelliJ o Eclipse.

##  Instalación y Configuración
Sigue estos pasos para tener el entorno listo:

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/yefervalencia/medicore-server.git
   cd medicore-server

2. **Ejecutar la API:**
    Una vez instaladas las dependencias, inicia el servidor con:
    ```bash
    ./mvnw spring-boot:run
  
3. **Endpoints de Prueba**
    Método: GET
    ```bash 
    URL: http://localhost:8080/api/v1/health/check?ciudad=Manizales

