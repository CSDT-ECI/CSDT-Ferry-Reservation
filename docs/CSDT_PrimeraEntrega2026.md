# CSDT — Primera Entrega 2026
### Sistema de Reserva de Ferry · Análisis de Deuda Técnica, Pruebas y Calidad de Software

> *"El código heredado sin pruebas no es simplemente código viejo — es código que no puede refactorizarse con confianza."*  
> — Michael Feathers, *Working Effectively with Legacy Code*

**Proyecto:** Sistema de Reserva de Ferry (fork de E-commerce con Spring Boot)  
**Tecnología:** Java 11 · Spring Boot · Hibernate/JPA · MySQL · JSP  
**Repositorio CSDT:** https://github.com/esteban0903/CSDT-Ferry-Reservation  

---

## Índice

1. [Contexto del proyecto y punto de partida](#1-contexto-del-proyecto-y-punto-de-partida)
2. [Deuda técnica en pruebas](#2-deuda-técnica-en-pruebas)
   - 2.1 [Estado inicial: pruebas de bajo valor](#21-estado-inicial-pruebas-de-bajo-valor)
   - 2.2 [Prácticas de testing debt identificadas](#22-prácticas-de-testing-debt-identificadas)
   - 2.3 [Inventario de cobertura inicial vs. final](#23-inventario-de-cobertura-inicial-vs-final)
3. [Estrategia de pruebas implementada](#3-estrategia-de-pruebas-implementada)
   - 3.1 [Por qué se usan mocks con Mockito](#31-por-qué-se-usan-mocks-con-mockito)
   - 3.2 [¿Los mocks hacen las pruebas "no reales"?](#32-los-mocks-hacen-las-pruebas-no-reales)
   - 3.3 [Pirámide de pruebas: unit, integration y DAO tests](#33-pirámide-de-pruebas-unit-integration-y-dao-tests)
   - 3.4 [Escenarios implementados](#34-escenarios-implementados)
4. [Evaluación de la estrategia y mejoras propuestas](#4-evaluación-de-la-estrategia-y-mejoras-propuestas)
5. [Modelos de calidad de software](#5-modelos-de-calidad-de-software)
   - 5.1 [ISO/IEC 25010 aplicado al proyecto](#51-isoiec-25010-aplicado-al-proyecto)
6. [Herramientas de calidad y análisis](#6-herramientas-de-calidad-y-análisis)
7. [Riesgos pendientes y recomendaciones técnicas](#7-riesgos-pendientes-y-recomendaciones-técnicas)
8. [Conclusiones](#8-conclusiones)

---

## 1. Contexto del proyecto y punto de partida

El proyecto es un fork de un sistema de e-commerce en Spring Boot, adaptado como sistema de reserva de ferry. El código heredado presentaba múltiples problemas estructurales que sirvieron como base de análisis para el curso:

| Problema identificado | Impacto |
|---|---|
| JDBC directo con credenciales hardcodeadas en controladores | Vulnerabilidad crítica de seguridad (OWASP A02, A03) |
| Contraseñas en texto plano sin cifrado | Violación de principios básicos de autenticación |
| Lógica de negocio duplicada y dispersa en controladores | Alta deuda técnica de mantenibilidad |
| Endpoint `updateProduct` con implementación vacía comentada | Funcionalidad inexistente en producción |
| Zero pruebas de servicios o controladores | Refactorización imposible sin riesgo de regresión |

El estado inicial reflejaba un proyecto que nunca fue pensado para evolucionar, sino para funcionar en un contexto de demostración. El objetivo del curso fue transformarlo en un proyecto con prácticas de calidad sostenibles.

---

## 2. Deuda técnica en pruebas

### 2.1 Estado inicial: pruebas de bajo valor

Al comenzar el análisis, el proyecto contaba con **3 clases de prueba y 7 tests**, todos concentrados en modelos y carga de contexto:

- `CartProductTest` — verifica constructores, getters y setters del modelo `CartProduct`  
- `CartProductIdTest` — verifica la identidad compuesta (equals y hashCode)  
- `JtSpringProjectApplicationTests` — smoke test que únicamente valida que el contexto Spring carga sin errores

**¿Por qué estas pruebas tienen bajo valor de negocio?**

Las pruebas de getters, setters, equals y hashCode verifican comportamiento que en la mayoría de los casos es generado automáticamente por el IDE o por anotaciones como `@Data` de Lombok. No ejercen ninguna regla de negocio, no detectan regresiones en flujos críticos y no ofrecen retroalimentación ante cambios en la lógica de la aplicación.

Un test que comprueba `cartProduct.getCart() == cart` no fallará nunca a menos que alguien elimine el getter; no detecta si la lógica de asignación de roles, validación de usuarios o manejo de inventario está rota.

> **Analogía:** es equivalente a inspeccionar la pintura de un barco para certificar que está en condiciones de navegar.

El smoke test de contexto tiene un valor marginalmente mayor: garantiza que la configuración de Spring no está rota. Sin embargo, tampoco verifica ningún comportamiento funcional.

### 2.2 Prácticas de testing debt identificadas

| # | Práctica de testing debt | Descripción en el proyecto |
|---|---|---|
| 1 | **Pruebas de bajo retorno** | Cobertura concentrada en getters/setters sin lógica |
| 2 | **Ausencia de pruebas de servicios** | Ningún test para `userService`, `productService` o `categoryService` |
| 3 | **Ausencia de pruebas de controladores** | Ningún test para flujos de registro, login, listado o actualización |
| 4 | **Ausencia de pruebas de ramas de error** | No se verifica qué ocurre si el DAO falla o si hay datos inválidos |
| 5 | **Acoplamiento con base de datos productiva** | Sin perfil de test aislado; las pruebas requerirían MySQL activo |
| 6 | **Documentación desactualizada** | Los README no reflejaban el estado real de las pruebas |
| 7 | **Sin pipeline de calidad activo** | El Jenkinsfile define la etapa `test` pero no había pruebas que validar |

### 2.3 Inventario de cobertura inicial vs. final

| Capa | Antes | Después |
|---|---|---|
| Modelos |  Parcial (getters/setters) |  Mantenido |
| Contexto Spring |  Smoke test |  Mantenido |
| Servicios (`userService`) |  Sin cobertura |  4 escenarios implementados |
| Servicios (`productService`) |  Sin cobertura |  3 escenarios implementados |
| Controladores (`UserController`) |  Sin cobertura |  4 escenarios implementados |
| Controladores (`AdminController`) |  Sin cobertura |  3 escenarios implementados |


---

## 3. Estrategia de pruebas implementada

### 3.1 Por qué se usan mocks con Mockito

Las nuevas pruebas de servicios y controladores utilizan **Mockito** con la anotación `@ExtendWith(MockitoExtension.class)` para aislar la unidad bajo prueba de sus dependencias externas.

```java
// Ejemplo real del proyecto — UserServiceTest.java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private userDao userDao;          // dependencia reemplazada por un mock

    @InjectMocks
    private userService userService;  // clase bajo prueba

    @Test
    void getUsersReturnsDaoResult() {
        when(userDao.getAllUser()).thenReturn(Arrays.asList(new User(), new User()));

        List<User> result = userService.getUsers();

        assertEquals(2, result.size());
        verify(userDao).getAllUser();  // verifica que la delegación ocurrió
    }
}
```

Los mocks se usan porque:

1. **Aislamiento:** permiten probar `userService` sin necesidad de una base de datos, un servidor de aplicaciones ni ningún recurso externo.
2. **Velocidad:** una suite de 50 unit tests con mocks se ejecuta en milisegundos; la misma suite contra una base de datos real puede tardar decenas de segundos.
3. **Determinismo:** un mock siempre responde exactamente lo que se le programa, eliminando flakiness por estado externo.
4. **Verificación de contratos:** `verify(userDao).saveUser(user)` confirma que el servicio efectivamente delegó la operación al DAO, validando el contrato entre capas.

### 3.2 ¿Los mocks hacen las pruebas "no reales"?

Esta es una pregunta frecuente y merece una respuesta precisa: **no, los mocks no hacen las pruebas menos válidas; cambian *qué* están verificando**.

Un unit test con mocks verifica la **lógica interna de una clase en aislamiento**: sus decisiones, sus ramas condicionales, el manejo de errores y los contratos con sus dependencias. Es completamente real en ese sentido.

Lo que un unit test con mocks **no verifica** es:

- Que las consultas HQL/SQL son sintácticamente correctas y devuelven lo esperado.
- Que el mapeo ORM entre entidades y tablas está configurado correctamente.
- Que Spring Security intercepta los endpoints con las reglas definidas.
- Que el JSON o el modelo enviado a la vista tiene la estructura esperada.

Para eso existen otros niveles de prueba, como se describe a continuación.

### 3.3 Pirámide de pruebas: unit, integration y DAO tests

```
           /\
          /  \       ← Integration / End-to-End tests
         /----\         (más lentos, más costosos, menos cantidad)
        /      \
       /--------\    ← Integration tests (MockMvc, H2)
      /          \
     /------------\  ← Unit tests con Mockito
    /              \     (rápidos, deterministas, mayor cantidad)
   /________________\
```

| Tipo | Tecnología sugerida | Qué verifica | Cuándo falla |
|---|---|---|---|
| **Unit test** | JUnit 5 + Mockito | Lógica de la clase en aislamiento | La lógica interna está rota |
| **Integration test de servicio** | Spring Boot Test + `@DataJpaTest` + H2 | Queries JPA/HQL reales contra BD en memoria | El mapping ORM o la query está mal |
| **Integration test web** | `@WebMvcTest` + MockMvc | El controlador maneja correctamente HTTP, modelos y redirecciones | El endpoint responde mal o Spring Security bloquea |
| **Integration test completo** | `@SpringBootTest` + TestContainers/H2 | Todo el stack integrado | Configuración de Spring, seguridad o BD falla en conjunto |

Las pruebas implementadas en este proyecto corresponden principalmente al nivel de **unit tests**, que es el punto de partida correcto al introducir pruebas en un proyecto sin cobertura previa.

### 3.4 Escenarios implementados

#### Servicios

| Clase | Escenario | Resultado esperado |
|---|---|---|
| `UserServiceTest` | `getUsers` delega en DAO | Retorna la lista del DAO |
| `UserServiceTest` | `addUser` con violación de integridad | Lanza `RuntimeException("Add user error")` |
| `UserServiceTest` | `checkUserExists` delega en DAO | Retorna el booleano del DAO |
| `UserServiceTest` | `checkLogin` retorna el usuario del DAO | Objeto `User` no nulo |
| `ProductServiceTest` | `getProducts` retorna lista del DAO | Lista de productos |
| `ProductServiceTest` | `updateProduct` asigna id de ruta antes de persistir | El id del producto coincide con el de la ruta |
| `ProductServiceTest` | `deleteProduct` delega y retorna resultado | Booleano del DAO |

#### Controladores

| Clase | Escenario | Resultado esperado |
|---|---|---|
| `UserControllerTest` | `newUseRegister` con username disponible | Redirige a `/login` |
| `UserControllerTest` | `newUseRegister` con username ya existente | Vista `register` con mensaje de error |
| `UserControllerTest` | `indexPage` sin productos | Vista con mensaje `"No products are available"` |
| `UserControllerTest` | `indexPage` con productos | Vista con atributo `products` poblado |
| `AdminControllerTest` | `getproduct` sin productos | Vista `products` con mensaje de aviso |
| `AdminControllerTest` | `getproduct` con datos | Vista `products` con atributo `products` |
| `AdminControllerTest` | `updateProduct` POST | Redirige a `/admin/products` |

#### Evidencia de implementación

- [JtProject/src/test/java/com/jtspringproject/JtSpringProject/services/UserServiceTest.java](../JtProject/src/test/java/com/jtspringproject/JtSpringProject/services/UserServiceTest.java)
- [JtProject/src/test/java/com/jtspringproject/JtSpringProject/services/ProductServiceTest.java](../JtProject/src/test/java/com/jtspringproject/JtSpringProject/services/ProductServiceTest.java)
- [JtProject/src/test/java/com/jtspringproject/JtSpringProject/controller/UserControllerTest.java](../JtProject/src/test/java/com/jtspringproject/JtSpringProject/controller/UserControllerTest.java)
- [JtProject/src/test/java/com/jtspringproject/JtSpringProject/controller/AdminControllerTest.java](../JtProject/src/test/java/com/jtspringproject/JtSpringProject/controller/AdminControllerTest.java)

---

## 4. Evaluación de la estrategia y mejoras propuestas

La estrategia adoptada es **adecuada como punto de partida** para un proyecto legacy sin cobertura previa. Introduce pruebas rápidas y deterministas que permiten comenzar a refactorizar con confianza. Sin embargo, para un proyecto Spring Boot en producción se recomienda avanzar hacia los siguientes niveles:

### Mejora 1 — Pruebas de integración con H2

Agregar `@DataJpaTest` con H2 en memoria para validar las queries HQL del DAO sin necesidad de MySQL:

```java
@DataJpaTest
class UserDaoIntegrationTest {
    @Autowired
    private userDao userDao;

    @Test
    void getUserByUsernameReturnsCorrectUser() {
        // H2 ejecuta la query HQL real y valida el mapping ORM
    }
}
```

**Dependencia a agregar en `pom.xml`:**
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

### Mejora 2 — Pruebas de controladores con MockMvc

`@WebMvcTest` carga únicamente la capa web de Spring (sin BD), permitiendo probar HTTP, redirecciones, modelos de vista y filtros de seguridad:

```java
@WebMvcTest(AdminController.class)
class AdminControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private productService productService;

    @Test
    void getProductsEndpointReturns200() throws Exception {
        when(productService.getProducts()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/admin/products"))
               .andExpect(status().isOk())
               .andExpect(view().name("products"));
    }
}
```

### Mejora 3 — Perfil de test separado

Crear `src/test/resources/application-test.properties` para que las pruebas nunca toquen la base de datos productiva:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

---

## 5. Modelos de calidad de software

### 5.1 ISO/IEC 25010 aplicado al proyecto

El estándar **ISO/IEC 25010** define características de calidad del producto de software. A continuación se analiza el estado del proyecto en las dimensiones más relevantes:

#### Mantenibilidad

| Sub-característica | Estado inicial | Estado tras intervención |
|---|---|---|
| **Modularidad** |  Bajo — lógica de negocio en controladores |  Parcial — separación de capas existe pero incompleta |
| **Reusabilidad** |  Bajo — JDBC hardcodeado, no reutilizable |  Mejora con servicios |
| **Analizabilidad** |  Bajo — sin pruebas, difícil saber qué rompe |  Mejora con suite de tests |
| **Modificabilidad** |  Alto riesgo — cualquier cambio puede romper flujos no cubiertos |  Reducido con pruebas de servicios y controladores |
| **Capacidad de prueba (Testability)** |  Bajo — dependencias directas hacían las clases no testeables |  Introducción de inyección de dependencias permite usar mocks |

#### Confiabilidad

| Sub-característica | Observación |
|---|---|
| **Madurez** | El proyecto presenta endpoints con lógica incompleta (`updateProduct` comentado), lo que representa un riesgo activo de fallos en producción. |
| **Tolerancia a fallos** | No existe manejo de errores consistente; el DAO retorna un objeto `User` vacío en lugar de lanzar una excepción tipada, lo que dificulta detectar fallos en capas superiores. |
| **Recuperabilidad** | Sin pruebas de ramas de error, no hay garantía de que el sistema se recupere correctamente ante datos inválidos. |

#### Seguridad

El modelo ISO/IEC 25010 incluye **Seguridad** como característica de primer nivel. En el proyecto se identificaron violaciones directas:

- Confidencialidad comprometida: contraseñas expuestas en el modelo de vista (`model.addAttribute("password", user.getPassword())`).
- Autenticidad debilitada: comparación de contraseñas en texto plano en el DAO, sin hashing.
- No repudio afectado: ausencia de logging de operaciones críticas.

---

## 6. Herramientas de calidad y análisis

### JaCoCo — Cobertura de código

JaCoCo genera reportes de cobertura de líneas, ramas e instrucciones directamente integrados con Maven. Con el plugin configurado en `pom.xml`, cada ejecución de `mvn clean test` produce un reporte HTML en `target/site/jacoco/index.html`.

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals><goal>prepare-agent</goal></goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals><goal>report</goal></goals>
        </execution>
    </executions>
</plugin>
```

**Valor para el proyecto:** permitiría cuantificar la brecha de cobertura real en el DAO y las ramas condicionales no cubiertas en los controladores.

### GitHub CodeQL — Análisis estático de seguridad y calidad

Además de JaCoCo, se integró **GitHub CodeQL** mediante un workflow de GitHub Actions ubicado en `.github/workflows/codeql-analysis.yml`. Esta herramienta realiza análisis estático sobre el código Java del proyecto y permite detectar vulnerabilidades, patrones inseguros y problemas de calidad antes de que lleguen a producción.

En este proyecto, CodeQL aporta valor especialmente porque el código heredado contiene riesgos que no siempre son visibles con pruebas unitarias, por ejemplo:

- acceso a base de datos con credenciales hardcodeadas;
- exposición de contraseñas en texto plano;
- uso de prácticas inseguras de autenticación;
- posibles code smells asociados a acoplamiento fuerte y lógica de negocio en controladores.

El workflow configurado ejecuta el análisis en `push` y `pull_request`, usando Java 11 y compilando el proyecto desde `JtProject/`, lo que lo hace compatible con la estructura real del repositorio.

**Valor para el proyecto:** complementa a JaCoCo porque no mide cobertura, sino que inspecciona la calidad y la seguridad del código fuente. En conjunto, ambas herramientas permiten observar dos dimensiones distintas: cuánto código se prueba y qué tan seguro o mantenible es.

### Comparación entre herramientas utilizadas

| Herramienta | Tipo de análisis | Aporte principal en el proyecto |
|---|---|---|
| **JaCoCo** | Cobertura dinámica de pruebas | Mide líneas y ramas ejecutadas por la suite de tests |
| **GitHub CodeQL** | Análisis estático de seguridad y calidad | Detecta vulnerabilidades y patrones de código riesgosos |
| **Mockito** | Aislamiento de dependencias en unit tests | Permite probar servicios y controladores sin depender de la base de datos |

---

## 7. Riesgos pendientes y recomendaciones técnicas

| Riesgo | Severidad | Recomendación técnica |
|---|---|---|
| **Pruebas de DAO sin cobertura** | Alta | Implementar `@DataJpaTest` con H2 para validar todas las queries HQL, especialmente `getUserByUsername` que fue corregida manualmente durante el setup. |
| **Sin pruebas de integración web** | Alta | Agregar `@WebMvcTest` con MockMvc para los endpoints de registro y login, que involucran Spring Security y redirecciones condicionadas. |
| **Endpoint `updateProduct` comentado** | Crítica | La lógica del método está vacía en producción. Es el primer candidato para implementar y cubrir con pruebas antes de cualquier despliegue. |
| **Sin perfil de test aislado** | Media | Crear `application-test.properties` con H2 para que las pruebas nunca dependan de la disponibilidad de MySQL local. |
| **Sin revisión continua de hallazgos de seguridad** | Media | Revisar periódicamente las alertas de GitHub CodeQL y convertir los hallazgos repetitivos en tareas de refactorización priorizadas dentro del backlog técnico. |
| **Sin reporte de cobertura automatizado** | Media | Integrar JaCoCo en el pipeline de Jenkins para que cada PR incluya el delta de cobertura. Establecer un umbral mínimo del 70% en capas de servicio. |
| **Contraseñas en texto plano** | Crítica (seguridad) | Migrar a `BCryptPasswordEncoder` en `SecurityConfiguration`. Requiere actualizar el seed de datos y agregar pruebas de autenticación con Spring Security Test. |
| **Logging insuficiente** | Media | Agregar `@Slf4j` y registrar operaciones críticas (login fallido, creación de usuario, cambio de contraseña) para cumplir con trazabilidad de seguridad. |

---

## 8. Conclusiones

El análisis del proyecto permitió aplicar de forma práctica los conceptos de deuda técnica en pruebas, estrategias de testing y modelos de calidad estudiados en el curso:

1. **La deuda técnica en pruebas es silenciosa pero costosa.** Un proyecto con pruebas solo de getters y setters tiene una falsa sensación de seguridad. La cobertura numérica sin valor de negocio es un indicador engañoso.

2. **Los unit tests con Mockito son el primer escalón, no el techo.** Proporcionan retroalimentación rápida y permiten comenzar a refactorizar. La madurez de la suite de pruebas se mide por cuántos niveles de la pirámide se cubren, no solo por el porcentaje de líneas.

3. **ISO/IEC 25010 ofrece un vocabulario preciso** para comunicar problemas de calidad más allá de "el código está mal". Hablar de testability, modifiability o confidentiality permite priorizar intervenciones con impacto medible.

4. **Las herramientas automatizadas (JaCoCo y GitHub CodeQL) son multiplicadores de esfuerzo**, no reemplazos del criterio de ingeniería. Su mayor valor está en hacerse parte del pipeline de CI para que la calidad sea una restricción continua, no una revisión puntual.

5. **La IA como asistente de calidad es una realidad presente.** Su uso en la generación de escenarios de prueba reduce la fricción inicial para equipos que comienzan a introducir testing en proyectos legacy.

---

*Bitácora consolidada por el equipo CSDT — 2026.*  
*Proyecto: Sistema de Reserva de Ferry · Curso: Calidad y Deuda Técnica en Software*
