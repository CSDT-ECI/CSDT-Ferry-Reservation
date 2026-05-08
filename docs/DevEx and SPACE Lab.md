# DevEx + SPACE Lab

> Bitacora de analisis del proyecto con enfoque en experiencia de desarrollo (DevEx) y productividad multidimensional (SPACE).

## Indice
- [Objetivos de aprendizaje](#objetivos-de-aprendizaje)
- [Contexto y alcance del analisis](#contexto-y-alcance-del-analisis)
- [Evidencia recolectada](#evidencia-recolectada)
- [Analisis del framework DevEx](#analisis-del-framework-devex)
- [Analisis del framework SPACE](#analisis-del-framework-space)
- [Hallazgos positivos y negativos](#hallazgos-positivos-y-negativos)
- [Oportunidades de mejora priorizadas](#oportunidades-de-mejora-priorizadas)
- [Metricas identificables (baseline y meta)](#metricas-identificables-baseline-y-meta)
- [Plan de seguimiento en bitacora](#plan-de-seguimiento-en-bitacora)
- [Conclusion](#conclusion)

## Resumen rapido

| Eje | Estado actual | Siguiente foco |
|---|---|---|
| DevEx | Mejoro el feedback con pruebas unitarias y CI | Reducir friccion de setup y carga cognitiva |
| SPACE | Alta actividad y avances de calidad visibles | Alinear actividad con impacto medible |
| Riesgo principal | Validacion aun parcial en integracion web/DB | Incorporar H2/MockMvc y quality gates graduales |

## Objetivos de aprendizaje
- Evaluar el estado actual del proyecto desde la experiencia del desarrollador (DevEx).
- Medir la productividad del equipo usando dimensiones del framework SPACE.
- Identificar fricciones tecnicas que afectan velocidad, calidad y colaboracion.
- Proponer mejoras concretas con metricas trazables para la bitacora del curso.

---

## Contexto y alcance del analisis
El analisis se realiza sobre el repositorio de un fork de e-commerce en Spring Boot, considerando las actividades ya ejecutadas en laboratorios previos:
- Identificacion de code smells y deuda tecnica.
- Primer ciclo de refactorizacion.
- Incorporacion, por parte del grupo CSDT, de pruebas unitarias en servicios y controladores.
- Integracion, por parte del grupo CSDT, de flujos de calidad con GitHub Actions (CodeQL y Sonar).

El objetivo de este documento no es repetir la deuda tecnica historica, sino evaluar como estas decisiones impactan la experiencia diaria de desarrollo y la productividad real del equipo.

---

## Evidencia recolectada

### Inventario tecnico observable
- 22 clases Java en codigo principal.
- 7 clases de pruebas automatizadas.
- 21 metodos anotados con `@Test`.
- 29 endpoints declarados (`@GetMapping`, `@PostMapping`, `@RequestMapping`).
- 14 usos de `System.out.println` en codigo productivo.
- 9 clases con nombre en `lowerCamelCase` (convencion inconsistente para clases Java).
- 2 usos de `DriverManager.getConnection` con credenciales hardcodeadas en controlador.

### Evidencia cualitativa en codigo y pipeline
- Endpoint de actualizacion de producto con implementacion incompleta:
  [AdminController.java](../JtProject/src/main/java/com/jtspringproject/JtSpringProject/controller/AdminController.java)
- Exposicion de password en modelo de vista:
  [UserController.java](../JtProject/src/main/java/com/jtspringproject/JtSpringProject/controller/UserController.java)
- Configuracion de DB con credenciales locales en properties:
  [application.properties](../JtProject/src/main/resources/application.properties)
- Pipeline Jenkins desalineado con el fork y con shell no portable para Windows:
  [jenkins file](../jenkins%20file)
- Workflows activos de calidad en GitHub Actions, agregados en el proceso de trabajo del grupo CSDT sobre el fork:
  [sonar.yml](../.github/workflows/sonar.yml)
  [codeql-analysis.yml](../.github/workflows/codeql-analysis.yml)

---

## Analisis del framework DevEx

Para este laboratorio usamos DevEx como evaluacion de friccion tecnica, velocidad del ciclo de feedback y confianza para cambiar codigo.

### 1) Flujo local y onboarding

**Positivo**
- Estructura de capas clara (controller, service, dao, model).
- Existe configuracion de build con Maven y documentacion base para ejecutar el proyecto.

**Negativo**
- Dependencia fuerte de configuracion manual de MySQL local.
- El archivo `docs/README.md` corresponde al contexto original y no refleja toda la evolucion del fork.

**Impacto DevEx**
- Aumenta tiempo de setup y frustra el primer contacto con el proyecto.

### 2) Inner loop (editar-probar-validar)

**Positivo**
- Ya existe una base de pruebas unitarias de servicios y controladores.
- El proyecto tiene CI con ejecucion de pruebas y analisis estatico.

**Negativo**
- Aun no hay pruebas de integracion web/DB para validar flujo real.
- Se mantiene logica de infraestructura dentro de controladores (JDBC directo).
- Existen funcionalidades incompletas que obligan a validacion manual.

**Impacto DevEx**
- El desarrollador recibe feedback parcial: rapido en unit tests, pero incompleto para riesgo real de regresion.

### 3) Carga cognitiva y mantenibilidad

**Positivo**
- El trabajo de labs anteriores mejoro la visibilidad de riesgos y genero evidencia tecnica.

**Negativo**
- Inconsistencia de nomenclatura (`cartDao`, `userService`) dificulta lectura uniforme.
- Mezcla de responsabilidades en controladores incrementa complejidad accidental.
- Uso de `System.out.println` en lugar de logging estructurado reduce trazabilidad.

**Impacto DevEx**
- Mayor esfuerzo mental para entender cambios y depurar incidencias.

### 4) Confiabilidad de herramientas

**Positivo**
- Se integraron CodeQL y Sonar en ramas de trabajo durante las actividades del grupo CSDT.

**Negativo**
- En Sonar se uso temporalmente `-Dsonar.coverage.exclusions=**` (decision del grupo CSDT para estabilizar CI y permitir continuidad de pushes), por lo que la cobertura no se esta usando aun como indicador de gate.
- Jenkins historico y GitHub Actions coexisten pero no estan completamente alineados (riesgo de doble verdad operativa).

**Impacto DevEx**
- Disminuye la confianza en los indicadores de calidad y en la trazabilidad de decisiones.

---

## Analisis del framework SPACE

SPACE evalua productividad de forma multidimensional:
- S: Satisfaction and well-being
- P: Performance
- A: Activity
- C: Communication and collaboration
- E: Efficiency and flow

### S - Satisfaction and well-being

**Positivo**
- Se observa avance progresivo en practicas de calidad (pruebas y analisis).

**Negativo**
- Fricciones de entorno (setup local de DB obligatorio) generan desgaste.

### P - Performance

**Positivo**
- Se agregaron tests que cubren reglas de servicios y controladores.

**Negativo**
- Persisten riesgos funcionales y de seguridad en codigo productivo (JDBC directo, password en modelo, endpoint incompleto).

### A - Activity

**Positivo**
- Evidencia de actividad tecnica en pipelines, pruebas y documentacion.

**Negativo**
- Alto volumen de actividad no siempre significa alto impacto inmediato (ejemplo: cobertura temporalmente excluida en Sonar mientras se estabiliza el pipeline).

### C - Communication and collaboration

**Positivo**
- Los laboratorios estan documentados y son reutilizables como evidencia de equipo.

**Negativo**
- Falta una bitacora consolidada por fecha/decision/resultado para reducir ambiguedad entre documentos.

### E - Efficiency and flow

**Positivo**
- Con unit tests actuales, el feedback de cambios puntuales mejora.

**Negativo**
- Sin pruebas de integracion, parte de la validacion sigue siendo manual y lenta.

---

## Hallazgos positivos y negativos

| Dimension | Puntos positivos | Puntos negativos |
|---|---|---|
| DevEx | Base de testing ya creada, CI en GitHub Actions | Friccion de setup local, indicadores de cobertura poco confiables |
| SPACE-S | Equipo con progreso visible en calidad | Frustracion por setup y deuda tecnica residual |
| SPACE-P | Mejora de calidad en capas service/controller | Riesgo funcional por codigo legacy aun activo |
| SPACE-A | Alta actividad de cambios y pipelines | Actividad no siempre alineada con impacto real |
| SPACE-C | Documentacion de labs existente | Falta una bitacora unica y trazable |
| SPACE-E | Feedback rapido en unit tests | Flujo interrumpido por validaciones manuales e inconsistencias |

---

## Oportunidades de mejora priorizadas

1. **Recuperar portabilidad del entorno local**
- Definir Maven minimo requerido en README y estandarizar setup local.
- Agregar perfil de test con H2 para no depender de MySQL local en pruebas.

2. **Mejorar calidad de feedback en CI**
- Retirar gradualmente la exclusion global de cobertura en Sonar y reportar cobertura real por paquete.
- Definir un unico flujo oficial (GitHub Actions o Jenkins) y dejar el otro como respaldo documentado.

3. **Reducir carga cognitiva**
- Normalizar nombres de clases a PascalCase en DAO y Services.
- Mover JDBC y SQL fuera de controladores hacia capa de acceso a datos.

4. **Aumentar confiabilidad funcional**
- Completar endpoint `updateProduct` con prueba automatizada de comportamiento esperado.
- Eliminar exposicion de password en modelos de vista y reforzar seguridad.

5. **Consolidar colaboracion**
- Mantener una bitacora unica por fecha con: decision, evidencia, impacto DevEx/SPACE y siguiente accion.

---

## Metricas identificables (baseline y meta)

| Metrica | Baseline actual | Meta sugerida (Lab siguiente) | Framework |
|---|---:|---:|---|
| Ratio clases de test / clases productivas | 7 / 22 = 31.8% | >= 45% | SPACE-P, SPACE-E |
| Test methods (`@Test`) | 21 | >= 35 | SPACE-P |
| Clases con naming inconsistente | 9 | 0 | DevEx, SPACE-E |
| JDBC directo en controladores | 2 ocurrencias | 0 | DevEx, SPACE-P |
| `System.out.println` en codigo productivo | 14 | <= 2 (migrar a logger) | DevEx, SPACE-E |
| Endpointes con logica incompleta conocidos | 1 | 0 | SPACE-P |
| Dependencia de DB local para testing | Alta | Baja (H2/Testcontainers) | DevEx, SPACE-E |

---

## Plan de seguimiento en bitacora

Formato sugerido por entrada:
- Fecha
- Cambio aplicado
- Metrica impactada
- Resultado observado
- Decision siguiente

Ejemplo de entrada:
- Fecha: 2026-03-21
- Cambio aplicado: Analisis DevEx + SPACE y definicion de baseline
- Metrica impactada: linea base de testing y mantenibilidad
- Resultado observado: 21 tests, 9 clases con naming inconsistente, 2 JDBC directos en controller
- Decision siguiente: priorizar refactor de controladores y normalizar setup local

---

## Conclusion
El proyecto muestra una evolucion real respecto a su estado inicial: hoy tiene mejor cobertura de pruebas unitarias, mayor visibilidad de calidad y una base de automatizacion en CI. Sin embargo, desde DevEx aun existen fricciones fuertes de entorno y mantenibilidad que afectan la velocidad de desarrollo. Bajo SPACE, la actividad del equipo es alta, pero debe alinearse mejor con resultados de performance y flow.

La recomendacion principal es entrar a una fase corta de estabilizacion tecnica (nomenclatura, cobertura real, eliminacion de JDBC en controladores y mejora del setup local) antes de continuar con nuevas funcionalidades. Esto aumentara productividad sostenible y reducira riesgo de regresiones en las siguientes entregas.
