# Code Smells and Refactoring Lab

## Indice
- Objetivos de aprendizaje
- Proyecto seleccionado
- Instrucciones de la actividad
- Alcance inicial
- Olores de codigo identificados
- Deuda tecnica observada
- Candidatos de refactorizacion
- Pruebas y CI
- Evidencia y enlaces

## Objetivos de aprendizaje
- Desarrollar habilidades para identificar y documentar olores de codigo y deuda tecnica en codigo heredado.
- Adquirir experiencia en tecnicas de refactorizacion y su aplicacion para mejorar la calidad del codigo.
- Mejorar las habilidades de colaboracion mediante la documentacion grupal y practicas de control de versiones.
- Comprender la importancia de las pruebas unitarias y la integracion continua en el desarrollo de software.
- Fomentar la creatividad en la documentacion del progreso del proyecto y en la utilizacion eficaz de la reduccion de la cuenta.

## Proyecto seleccionado
- Nombre del proyecto: Sistema de Reserva de Ferry
- Tecnologia: Java + Spring Boot + MySQL + JSP
- Repositorio original (publico): https://github.com/jaygajera17/E-commerce-project-springBoot
- Fork en la organizacion CSDT: https://github.com/esteban0903/CSDT-Ferry-Reservation
- Motivo de seleccion: codigo heredado con malas practicas, sin pruebas unitarias y sin CI.

## Instrucciones de la actividad
- Documentar el progreso del curso en [Debt Breakers_CSDT-2026.md](Debt%20Breakers_CSDT-2026.md).
- Usar este documento para registrar olores de codigo, deuda tecnica, candidatos de refactorizacion y avances.
- Usar el laboratorio de Markdown si es necesario: https://classroom.github.com/a/XgNKf2U3
- Trabajar en una rama hija de main y enviar pull request para revision.

## Alcance inicial
- Registrar una primera lista de olores de codigo y deuda tecnica.
- Definir candidatos de refactorizacion con justificacion.
- Proponer un plan minimo de pruebas y CI.

## Code smells identificados (primera actividad)
- Acceso a base de datos dentro de controladores (mezcla de capas).
- Credenciales hardcodeadas en codigo.
- Manejo de contrasenas en texto plano.
- Logica de negocio dispersa y duplicada en controladores.
- Endpoints con logica incompleta.
- Consultas HQL inconsistentes.

## Deuda tecnica observada
- Ausencia de pruebas unitarias y de integracion.
- Ausencia de pipeline de CI.
- Configuracion de base de datos insegura para entornos reales.
- Nombres de clases y paquetes con convenciones inconsistentes.

## Candidatos de refactorizacion
- Extraer acceso a datos a repositorios/servicios y eliminar JDBC directo en controladores.
- Encriptar contrasenas y usar el flujo de autenticacion de Spring Security.
- Unificar queries y entidades HQL.
- Crear DTOs para evitar exponer datos sensibles en vistas.
- Normalizar nombres de clases (PascalCase) y paquetes (lowercase).

## Checklist de entrega
- Inventario de code smells documentado.
- Deuda tecnica priorizada.
- Lista de refactorizaciones candidatas.
- Enlaces al repo base y al fork.
- Trabajo en rama hija y PR creado.

## Pruebas y CI
- Definir pruebas unitarias para servicios y repositorios.
- Agregar pruebas de integracion basicas para endpoints criticos.
- Configurar CI para ejecutar pruebas en cada pull request.

## Correcciones para compilar y ejecutar
- Se agrego `entityManagerFactory` para que Spring Data JPA inicialice repositorios.
	- Archivo: [JtProject/src/main/java/com/jtspringproject/JtSpringProject/HibernateConfiguration.java](JtProject/src/main/java/com/jtspringproject/JtSpringProject/HibernateConfiguration.java)
- Se ajusto el encoder de passwords a texto plano para usar el seed inicial.
	- Archivo: [JtProject/src/main/java/com/jtspringproject/JtSpringProject/configuration/SecurityConfiguration.java](JtProject/src/main/java/com/jtspringproject/JtSpringProject/configuration/SecurityConfiguration.java)
- Se corrigio HQL de `User` a `CUSTOMER` en `getUserByUsername`.
	- Archivo: [JtProject/src/main/java/com/jtspringproject/JtSpringProject/dao/userDao.java](JtProject/src/main/java/com/jtspringproject/JtSpringProject/dao/userDao.java)
- Se actualizo la clave de BD en `application.properties` para MySQL local.
	- Archivo: [JtProject/src/main/resources/application.properties](JtProject/src/main/resources/application.properties)
- Se insertaron usuarios seed manualmente en MySQL (admin/lisa).
	- Script ejecutado:
```sql
USE ecommjava;
INSERT INTO CUSTOMER(address, email, password, role, username) VALUES
	('123, Albany Street', 'admin@nyan.cat', '123', 'ROLE_ADMIN', 'admin'),
	('765, 5th Avenue', 'lisa@gmail.com', '765', 'ROLE_NORMAL', 'lisa');
```

## Evidencia con fragmentos

- JDBC directo y credenciales hardcodeadas en controlador: [AdminController profileDisplay](JtProject/src/main/java/com/jtspringproject/JtSpringProject/controller/AdminController.java#L194-L233)
```java
Class.forName("com.mysql.jdbc.Driver");
Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecommjava","root","");
PreparedStatement stmt = con.prepareStatement("select * from users where username = ?"+";");
```

- Exposicion de password en la vista (modelo): [UserController profileDisplay](JtProject/src/main/java/com/jtspringproject/JtSpringProject/controller/UserController.java#L117-L128)
```java
model.addAttribute("password", user.getPassword());
```

- Logica incompleta en update de producto: [AdminController updateProduct](JtProject/src/main/java/com/jtspringproject/JtSpringProject/controller/AdminController.java#L157-L163)
```java
@RequestMapping(value = "products/update/{id}",method=RequestMethod.POST)
public String updateProduct(@PathVariable("id") int id ,@RequestParam("name") String name,@RequestParam("categoryid") int categoryId ,@RequestParam("price") int price,@RequestParam("weight") int weight, @RequestParam("quantity")int quantity,@RequestParam("description") String description,@RequestParam("productImage") String productImage)
{
//		this.productService.updateProduct();
	return "redirect:/admin/products";
}
```

- Comparacion de password en DAO y retorno de objeto vacio: [userDao getUser](JtProject/src/main/java/com/jtspringproject/JtSpringProject/dao/userDao.java#L49-L60)
```java
if(password.equals(user.getPassword())) {
	return user;
}else {		
	return new User();
}
```

- Configuracion de DB con root y password vacio: [application.properties](JtProject/src/main/resources/application.properties#L28-L32)
```properties
db.url= jdbc:mysql://localhost:3306/ecommjava?createDatabaseIfNotExist=true
db.username= root
db.password=root123
```

