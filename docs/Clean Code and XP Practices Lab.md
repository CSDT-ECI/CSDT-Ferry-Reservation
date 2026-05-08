# Clean Code + XP Practices

## Indice

- Objetivos de aprendizaje
- Descripcion general del proyecto
- Análisis de Clean Code y Principios de Programación
- Análisis de Principios SOLID
- Evaluación de Prácticas XP
- Principales Problemas Detectados
- Recomendaciones Técnicas
- Conclusión Final


## Objetivos de aprendizaje

- Desarrollar habilidades para analizar código heredado utilizando principios de Clean Code, SOLID y prácticas de Extreme Programming (XP).
- Adquirir experiencia en la identificación de problemas de diseño, código y prácticas de desarrollo en proyectos existentes.

---

## Descripcion general del proyecto

El proyecto seleccionado es un fork de un sistema de e-commerce desarrollado en Java utilizando el framework Spring Boot. El código real permite gestionar usuarios, categorías, productos, carrito y panel de administración. El proyecto utiliza una base de datos MySQL para almacenar la información y emplea JSP para la capa de presentación.

El proyecto sigue una arquitectura en capas:

- Controller
- Services
- DAO
- Model
- Repository
- Configuración Hibernate

Se utiliza:
- Spring MVC
- Spring Security
- JPA
- MySQL

Esta separación demuestra una intención de arquitectura limpia, aunque presenta inconsistencias en su implementación.

---

## Análisis de Clean Code

---
## Caracterisiticas positivas
- ### Separación en capas
Existe una división clara entre controladores, servicios y DAO.

- ### Inyección de dependencias
Se utiliza `@Autowired`, lo cual reduce acoplamiento directo.
```java
@Autowired
public AdminController(userService userService, categoryService categoryService, productService productService)
```

- ### Uso de @EmbeddedId
En `CartProductId` se implementa correctamente `Serializable`, `equals()` y `hashCode()`.

```java
@Embeddable
public class CartProductId implements Serializable
```
---

## Caracteristicas negativas

- ### Código no enfocado (Controladores sobrecargados)



```java
@GetMapping("profileDisplay")
public String profileDisplay(Model model) {
    Connection con = DriverManager.getConnection(...);
}
```

- ### Violaciond de DRY

Repetido en varios métodos.
DRY establece que el conocimiento no debe duplicarse.

```java
DriverManager.getConnection("jdbc:mysql://localhost:3306/ecommjava","root","");
```

- ### Exposición de datos sensibles

```java
model.addAttribute("password", user.getPassword());
```

- ### Código incompleto

```java
@RequestMapping(value = "products/update/{id}",method=RequestMethod.POST)
public String updateProduct(@PathVariable("id") int id ,@RequestParam("name") String name,@RequestParam("categoryid") int categoryId ,@RequestParam("price") int price,@RequestParam("weight") int weight, @RequestParam("quantity")int quantity,@RequestParam("description") String description,@RequestParam("productImage") String productImage)
{
//		this.productService.updateProduct();
    return "redirect:/admin/products";
}
```  
- ### Violacion del KISS
Condicional innecesario.
KISS indica que la solución más simple es la mejor.

```java
if(category.getName().equals(category_name)) {
    return "redirect:categories";
}else {
    return "redirect:categories";
}
```

## Análisis de Principios SOLID

- ### SRP (Single Responsibility Principle)

Los controladores realizan múltiples tareas:
- Manejo HTTP
- Acceso a base de datos
- Seguridad
- Lógica de negocio

Una clase debe tener una sola razón para cambiar.

- ### OCP (Open Closed Principle)

Para agregar nuevas funcionalidades es necesario modificar directamente los controladores.

No están diseñados para extensión sin modificación.

- ### LSP (Liskov Substitution Principle)

No se identifican violaciones directas, aunque la arquitectura anémica debilita el modelo.

- ### ISP (Interface Segregation Principle)

No se evidencia uso claro de interfaces en DAO, lo que puede generar dependencias innecesarias.

- ### DIP (Dependency Inversion Principle)
```java
@Autowired
private categoryDao categoryDao;

Se depende de clases concretas en lugar de abstracciones.
```


## Evaluación de Prácticas XP

- ### Pair Programming
   No se evidencia en el repositorio, aunque es posible que se haya realizado de manera informal.
- ### Test-Driven Development (TDD)
   No se encuentran pruebas unitarias o de integración en el proyecto, lo que sugiere que no se siguió esta práctica.
- ### Refactoring
   No se evidencia un proceso de refactorización sistemática, aunque se identifican áreas claras para mejorar el diseño y la calidad del código.


## Principales Problemas Detectados

- Violación SRP en controladores
- Código duplicado
- Exposición de contraseñas
- Services anémicos
- Falta de pruebas automatizadas
- Inyección por campo en lugar de constructor
- Configuración redundante



## Recomendaciones Técnicas

- Separar controladores por dominio funcional.
- Implementar inyección por constructor en todos los servicios.
- Eliminar código comentado.
- Implementar pruebas unitarias con JUnit y Mockito.
- Agregar validaciones en entidades.
- Usar pool de conexiones (HikariCP).
- Crear excepciones personalizadas.


## Conclusiones

El proyecto presenta una arquitectura base adecuada con separación en capas y uso de tecnologías empresariales como Spring y Hibernate. Sin embargo, se identifican múltiples violaciones a principios de Clean Code y SOLID, especialmente en la sobrecarga de responsabilidades en controladores, duplicación de código, servicios anémicos y ausencia de pruebas automatizadas.

Desde la perspectiva de XP, no se evidencia aplicación de TDD ni refactorización continua, lo cual incrementa la deuda técnica. Se concluye que el sistema posee una base estructural correcta, pero requiere refactorización para alcanzar estándares profesionales de calidad, mantenibilidad y seguridad.