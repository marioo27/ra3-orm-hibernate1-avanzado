package es.ciudadescolar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ciudadescolar.servicios.AlumnoServicio;
import es.ciudadescolar.util.JPAUtil;

/**
    En esta aplicacion usaremos una arquitectura de responsabilidades multicapa (buenas practicas) 

            main():  Inicia la aplicacion, coordina el flujo de ejecucion de los servicios, capturar excepciones de alto nivel y cierra recursos criticos al final (EntityManagerFactory)
            ↓        No contiene la logica de negocio. No accede directamente a la base de datos.
            service: Es la capa de negocio: Implementar reglas y casos de negocio. Gestiona transacciones. Coordina multiples DAOs en una transaccion. Registra informacion relevante al negocio en el log.
            ↓        No contiene queries JPQL o SQL. No acede directamente a EntityManager (solo via DAOs) salvo para la gestion de transacciones.
            dao:     Es la capa de persistencia: Encapsula todas las operaciones de acceso a la BD (CRUD y consultas especificas). 
            ↓        No contiene logica de negocio. No decide reglas de transaccion ni Logging (salvo excepciones tecnicas de persistencia).
            domain   Es el modelo de datos (entidad JPA): Representa una tabla de la base de datos en forma de objeto Java. Define campos, relaciones, restricciones

            JPAUtil de forma transversal: Crea y mantiene un unico EntityManagerFactory (EMF) y provee metodos para obtener un EntityManager nuevo cuando se necesite. Cierra el EMF al finalizar la aplicacion.
*/
public class Main 
{
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) 
    {
        LOG.debug ("Inicio de aplicacion");
        try 
        {
            AlumnoServicio service = new AlumnoServicio();
            // Al acceder por primera vez a cualquier metodo estatico, Java inicializa los campos estaticos.
            // Por tanto el EntityManagerFactory de JPAUtil se instanciara cuando el service haga JPAUtil.getEntityManager() por primera vez.
            service.registrarAlumno("Anchon Garcia", "agarcia@ciudadescolar.es");;
        }
        catch (Exception e)
        {
            // Todas las excepciones (checked + unchecked)
            // Aseguramos que cualquier error en la aplicacion se capture antes de cerrar recursos criticos (EMF).
            LOG.error ("Error en la aplicacion: "+e.getMessage());
        } 
        finally 
        {
            JPAUtil.close(); 
        }
        LOG.debug ("Fin de aplicacion");
    }
}
