# Deuda técnica en procesos

## Índice

- [1. Descripción](#1-descripción)
- [2. Pipeline de CI](#2-pipeline-de-ci)
- [3. Etapas del Proceso](#3-etapas-del-proceso)
  - [3.1 Build](#31-build)
  - [3.2 Pruebas Unitarias](#32-pruebas-unitarias)
  - [3.3 Cobertura (JaCoCo)](#33-cobertura-jacoco)
  - [3.4 Análisis de Calidad (SonarCloud)](#34-análisis-de-calidad-sonarcloud)
- [4. Seguridad (OWASP)](#4-seguridad-owasp)
- [5. Problemas y Soluciones](#5-problemas-y-soluciones)
- [6. Resultados](#6-resultados)
- [7. Conclusión](#7-conclusión)

## 1. Descripción

En este proyecto se implementó un proceso de Integración Continua (CI) utilizando GitHub Actions. El objetivo fue automatizar la construcción del proyecto, la ejecución de pruebas, el análisis de calidad y la revisión de seguridad.

---

## 2. Pipeline de CI

El workflow se configuró para ejecutarse en cada push y pull request sobre múltiples ramas:

```
  push:
    branches: [ "main", "lab3", "lab4", "lab5" ]
  pull_request:
    branches: [ "main", "lab3", "lab4", "lab5" ]
```

Esto permite validar cambios sin depender de la rama principal.

---

## 3. Etapas del Proceso

### 3.1 Build

Se compila el proyecto con Maven:

```
mvn clean install
```

Esto garantiza que el código compile correctamente.

---

### 3.2 Pruebas Unitarias

Se ejecutan pruebas con JUnit y Mockito:

```
mvn test
```

Se probaron principalmente controladores y servicios, evitando dependencias externas mediante mocks.

---

### 3.3 Cobertura (JaCoCo)

Se mide la cobertura de código:

```
mvn verify
```

Se logró aumentar la cobertura desde aproximadamente 21% hasta superar el mínimo requerido.

---

### 3.4 Análisis de Calidad (SonarCloud)

Se integró SonarCloud para evaluar calidad del código:

```
mvn verify sonar:sonar
```

Se analizaron métricas como bugs, code smells y cobertura.

---

## 4. Paso Adicional: Seguridad (OWASP)

Se integró OWASP Dependency Check para detectar vulnerabilidades:

```
mvn org.owasp:dependency-check-maven:check
```

Debido a restricciones de NVD, se configuró para no detener el pipeline:

```
mvn org.owasp:dependency-check-maven:check || true
```

---

## 5. Problemas y Soluciones

- Error de Maven: se ejecutaba fuera del directorio con pom.xml  
  Solución: usar el directorio correcto

- Baja cobertura: pocos tests iniciales  
  Solución: agregar pruebas en servicios y controladores

- Fallos en DAO: uso de Hibernate sin contexto  
  Solución: usar mocks y evitar pruebas directas a DAO

- Error OWASP (403 NVD): falta de API Key  
  Solución: permitir que el paso no rompa el pipeline

---

## 6. Resultados

- Pipeline automatizado funcionando correctamente  
- Pruebas ejecutándose en cada cambio  
- Cobertura de código incrementada  
- Integración de herramientas de calidad y seguridad  

## Resumen del Pipeline

| Etapa              | Herramienta        | Comando Principal                                      | Propósito |
|------------------|------------------|------------------------------------------------------|----------|
| Build            | Maven            | mvn clean install                                    | Compilar el proyecto |
| Tests            | JUnit / Mockito  | mvn test                                             | Validar lógica del sistema |
| Coverage         | JaCoCo           | mvn verify                                           | Medir cobertura de código |
| Code Analysis    | SonarCloud       | mvn verify sonar:sonar                               | Analizar calidad del código |
| Seguridad        | OWASP            | mvn dependency-check:check || true                   | Detectar vulnerabilidades |
| Seguridad Avanzada | CodeQL         | GitHub Actions                                       | Análisis estático de seguridad |
---

## 7. Conclusión

La implementación de un proceso de Integración Continua en este proyecto permitió automatizar tareas fundamentales del ciclo de desarrollo, como la compilación, ejecución de pruebas, análisis de calidad y validación de seguridad. 
Uno de los principales aprendizajes fue la importancia de las pruebas unitarias bien estructuradas. Al utilizar herramientas como Mockito, se logró desacoplar la lógica del negocio de dependencias externas, permitiendo pruebas más rápidas, confiables y mantenibles. Esto fue clave para aumentar la cobertura de código y cumplir con los umbrales establecidos.

Por otro lado, la integración de herramientas como JaCoCo y SonarCloud permitió obtener métricas objetivas sobre la calidad del código, facilitando la identificación de problemas como código duplicado, malas prácticas o posibles errores. Esto contribuye a un código más limpio, entendible y sostenible en el tiempo.

Adicionalmente, la inclusión de OWASP Dependency Check y CodeQL introduce un enfoque de seguridad dentro del pipeline, lo cual es fundamental en el desarrollo moderno. Aunque se presentaron limitaciones técnicas como el acceso restringido a la base de datos NVD, se logró integrar el análisis sin afectar la ejecución del pipeline, demostrando la capacidad de adaptación ante este tipo de escenarios.

En conclusión, este proceso no solo cumple con los requerimientos planteados, sino que también establece una base sólida para prácticas de desarrollo profesional, integrando automatización, calidad y seguridad en un flujo continuo.