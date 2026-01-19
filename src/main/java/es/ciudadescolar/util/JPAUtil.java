package es.ciudadescolar.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static final Logger LOG = LoggerFactory.getLogger(JPAUtil.class);

    private static final EntityManagerFactory emf;
    static 
    {
        LOG.debug("Creando pool de conexiones...");

        emf = Persistence.createEntityManagerFactory("PersistenciaInstituto");

    }    

    public static EntityManager getEntityManager() 
    {
        LOG.debug("Nueva conexion del pool solicitada");
        return emf.createEntityManager();
    }

    
    public static void close() {
        if (emf.isOpen())
        {
            emf.close();
            LOG.debug("Se ha cerrado el pool de conexiones con la BD");
        }
    }

}