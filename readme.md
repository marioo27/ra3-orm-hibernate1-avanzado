# 📝 ORM - Hibernate + JPA + Arquitectura de capas

👨‍🏫 Asignatura: Acceso a Datos

🧑‍💻 Profesor: José Sala Gutiérrez

📆 Curso: [2025/2026]

---

Este proyecto replica la lógica desarrollada en el proyecto [RA3-ORM-HIBERNATE1](https://github.com/DAM2-AccesoDatos/ra3-orm-hibernate1), pero en este caso, se utiliza una **Arquitectura de capas** profesional para interactuar con una BD de relacional **separando responsabilidades**. La clase Main inicia todo, las clases Service gestionan el negocio, las clases DAO persisten los datos, las clases POJO (Entity) representan los datos y una clase JPAUtil gestiona EMF/EM.

La idea, tal y como hacemos con la gestión del log, es independizar nuestro código de las implementaciones de ORM y así poder a futuro cambiar Hibernate por EclipseLink u OpenJPA sin necesidad de tocar el código ni anotaciones. Es por eso que únicamente usaremos anotaciones e interfaces de la JPA (estandar). No deberíamos por tanto tener ningún *import* de la API *org.hibernate*

El objetivo es **familiarizarse con las aplicaciones multicapa y reconocer los beneficios de su uso** para interactuar con bases de datos relacionales siguiendo las directrices de **buenas prácticas**. A saber:

- Separación clara de responsabilidades
- Transacciones solo en Service
- Persistencia solo en DAO
- POJO (Entity) solo representa datos, sin lógica de negocio
- Logging de negocio en Service, logging de errores técnicos en DAO
- Main solo orquestador general y cierra recursos

## 🔧 Tecnologías utilizadas

- Maven + Java 17+
- Pool HikariCP
- Log slf4j + Logback
- ORM Hibernate + JPA
- Base de datos H2 (modo persistente)
- Database Client (extensión VSCode) + Driver H2
- JPA (EntityManagerFactory, EntityManager, EntityTransaction, TypedQuery)

## ✅ Características

- Se utiliza una clase POJO `Alumno` como contenedor de datos de la entidad asociada, con atributos de instancia y métodos getter/setter. Representa la tabla de BD como un objeto Java.

- Se utiliza una clase DAO `AlumnoDAO` encargada de encapsular y manejar la operativa de persistencia de datos de la entidad asociada, como consultar, guardar, actualizar o eliminar información de una base de datos.
  
- Se utiliza una clase SERVICE `AlumnoServicio` encargada de implementar todas las reglas y casos de negocio que atañen a la clase relacionada. los SERVICE pueden coordinar múltiples DAOs en operaciones transaccionales y son los responsables de gestionar transacciones (begin, commit, rollback) también son el mejor lugar para registrar trazas (logs) de negocio.

- Se utiliza una clase de utilería `JPAUtil` encargada de crear y mantener un único EntityManagerFactory (EMF), de proveer EntityManager y finalmente de cerrar EMF al finalizar la aplicación.

- La clase `Main` solo se encarga de iniciar la aplicación, manejar excepciones de alto nivel y cerrar recursos generales antes de finalizar (EMF). No contiene lógica de negocio o acceso directo a la BD.

## 📁 Estructura del proyecto

```text
src/
├─ main/
│ ├─ es/ciudadescolar/
│ │ ├─ persistencia/
│ │ │   └─ AlumnoDAO.java
│ │ ├─ modelo/
│ │ │   └─ Alumno.java
│ │ ├─ servicios/
│ │ │   └─ AlumnoServicio.java
│ │ ├─ util/
│ │ │   └─ JPAUtil.java
│ │ └─ Main.java
│ └─ resources/
│    ├─ META-INF
│    │    └─ persistence.xml   
│    └─ logback.xml
├─ .gitignore
├─ pom.xml
└─ readme.md
```
## Arquitectura de Capas – Proyecto JPA/Hibernate

### 🏁 1. Main

- **Responsabilidad:**
  - Iniciar la aplicación
  - Manejar excepciones de alto nivel
  - Cerrar recursos críticos (EntityManagerFactory, pool HikariCP)
- **No debe:** contener lógica de negocio o acceso directo a la BD

### ⚙️ 2. Service

- **Responsabilidad:**
  - Implementar reglas y casos de negocio
  - Coordinar múltiples DAOs en operaciones transaccionales
  - Gestionar transacciones (begin, commit, rollback)
  - Registrar logs de negocio/auditoría
- **No debe:** contener queries JPQL o SQL, ni acceder directamente a EntityManager salvo gestión de transacciones

### 🗄️ 3. DAO (Data Access Object)

- **Responsabilidad:**
  - CRUD y consultas específicas usando EntityManager
  - Manejar excepciones técnicas de persistencia
- **No debe:** contener lógica de negocio

### 🧩 4. Entity

- **Responsabilidad:**
  - Representar tablas de BD como objetos Java
  - Definir atributos, relaciones y restricciones JPA
  - Opcional: toString(), equals(), hashCode() para depuración
- **No debe:** contener lógica de negocio compleja ni acceder a DAOs

### 🔑 5. JPAUtil

- **Responsabilidad:**
  - Crear y mantener un único EntityManagerFactory (EMF)
  - Proveer métodos para obtener EntityManager
  - Cerrar EMF al finalizar la aplicación
- **No debe:** contener lógica de negocio o queries JPQL

## 🧰 Diagrama conceptual de flujo (flujo de ejecución)

```text
+----------------+
|     Main       |
+----------------+
       |
       v
+----------------+
|    Service     |
+----------------+
       |
       v
+----------------+
|      DAO       |
+----------------+
       |
       v
+----------------+
|     Entity     |
+----------------+

+----------------+
|    JPAUtil     |
+----------------+
```
