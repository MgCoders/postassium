package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.Material;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by pablo on 21/01/18.
 */
@Stateless
public class MaterialDao extends AbstractDao<Material, Long> {

    @PersistenceContext
    EntityManager em;

    @Override
    public Class<Material> getEntityClass() {
        return Material.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Material> findByPattern(String pattern) {
        Query query = em.createQuery("SELECT m FROM Material m WHERE UPPER(m.nombre) LIKE UPPER(:nombre)");
        query.setParameter("nombre", "%" + pattern + "%");
        return query.getResultList();
    }

}
