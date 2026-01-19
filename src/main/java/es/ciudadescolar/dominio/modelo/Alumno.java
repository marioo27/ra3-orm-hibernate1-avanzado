package es.ciudadescolar.dominio.modelo;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** 
 * Nunca meter los metodos de guardar, modificar o consultar un alumno en esta clase simulando un DAO.
 * Es una mala practica porque viola el principio "Single Responsibility Principle", es decir haria dos cosas:
 * - Representar un alumno
 * - Acceder a la base de datos
 * Debemos crear otra clase auxiliar (AlumnoRepositorio o AlumnoDAO) donde implementemos los metodos de interaccion con la clase Alumno.
 * 
 * Para poder gestionar transacciones que aglutinen la invocacion de varios metodos que interactuen con la BD, siempre deberiamos
 * evitar crear y cerrar EntityManager en esos metodos (error de disenio), debemos pasarle al constructor un EntityManager ya creado en 
 * una clase superior (AlumnoService o Main) que haga labores de orquestador. 
 * 
 * En la capa de servicio es donde se cierran los EntityManager. Usamos uno por cada caso de uso. Porque ahi es donde:
 * - Empieza la transaccion
 * - Termina la transaccion
 * - Se sabe cuando acaba el trabajo
 * 
 * En el Main como orquestador final de la aplicacion es donde debemos cerrar el EntityManagerFactory.
*/

@Entity
@Table(name = "alumno")
public class Alumno implements Serializable
{

    private static final long serialVersionUID = 1L;
    
    // Por tratarse de la PK simple (!= compuesta) y ademas querer que sea auto_increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // la columna se llamara IGUAL que el atributo y sera OBLIGATORIO
    @Column(name="nombre", nullable = false)
    private String nombre;

    // la columna se llamara distinto del atributo y sera unique
    @Column(name="correoElectronico", unique = true)
    private String email;

    public Alumno(){}

    public Alumno(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Alumno other = (Alumno) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (nombre == null) {
            if (other.nombre != null)
                return false;
        } else if (!nombre.equals(other.nombre))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Alumno [id=" + id + ", nombre=" + nombre + ", email=" + email + "]";
    }

      

}
