package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.MarcaEquipo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by msteglich on 8/24/18.
 */
@Stateless
public class MarcaEquipoDao extends AbstractDao<MarcaEquipo, Long>{

    @PersistenceContext
            EntityManager em;

    @Override
    public Class<MarcaEquipo> getEntityClass() {
        return MarcaEquipo.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }


    public List<MarcaEquipo> findByPattern(String pattern) {
        Query query = em.createQuery("SELECT m FROM MarcaEquipo m WHERE UPPER(m.nombre) LIKE UPPER(:pat)");
        query.setParameter("pat", "%" + pattern + "%");
        return query.getResultList();
    }


}
