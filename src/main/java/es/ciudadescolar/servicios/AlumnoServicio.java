package es.ciudadescolar.servicios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ciudadescolar.dominio.modelo.Alumno;
import es.ciudadescolar.persistencia.dao.AlumnoDAO;
import es.ciudadescolar.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class AlumnoServicio 
{
    private static final Logger LOG = LoggerFactory.getLogger(AlumnoServicio.class);

    public void registrarAlumno(String nombreAlumno, String emailAlumno) 
    {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction trans = em.getTransaction();

        try 
        {
            trans.begin();
            
            // Podriamos tener varios DAOs, todos ellos con el mismo EntityManager y bajo la misma transaccion
            AlumnoDAO alumnoDAO = new AlumnoDAO(em);
            
            // 1º Buscamos alumno existente o crear
            Alumno alumno = alumnoDAO.buscarPorEmail(emailAlumno);
            if (alumno == null) 
            {
                alumno = new Alumno(nombreAlumno,emailAlumno);
                alumnoDAO.guardar(alumno);
            }
            else
                LOG.warn("El alumno con email ["+emailAlumno+"] ya estaba registrado");

            trans.commit();
            LOG.info("Alumno registrado: "+ alumno.toString());
        } 
        catch (RuntimeException e) 
        {
            LOG.error("Error durante la transaccion: "+ e.getMessage());

            if (trans != null && trans.isActive()) 
            {
                trans.rollback();
                LOG.debug("Rollback de la transaccion");
   
            }
            throw e; // Propagamos error al main o a la capa superior
        } 
        finally 
        {
            try 
            {
                if (em != null && em.isOpen()) 
                {
                    em.close();
                    LOG.debug("Cerrado EntityManager");
                }
            } 
            catch (RuntimeException e) 
            {
                LOG.error("Error cerrando EntityManager:: "+ e.getMessage());
                // La transaccion ya se ha intentado commit o rollback. 
                // Aqui no propagamos la excepcion para evitar ocultar la excepcion original que pudo motivar el rollback o fallo de negocio.
            }
        }
     }
}
