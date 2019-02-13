package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.Material;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.QueryParam;
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
        Query query = em.createQuery("SELECT m FROM Material m " +
                "WHERE UPPER(m.nombre) LIKE UPPER(:nombre) " +
                "ORDER BY m.codigo ASC");
        query.setParameter("nombre", "%" + pattern + "%");
        return query.getResultList();
    }

    public List<Material> findPage(int limit, int offset) {
        Query query = em.createQuery("SELECT m FROM Material m ORDER BY m.codigo ASC");
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public List<Material> findPageFilter(int limit, int offset, String filter) {
        Query query = em.createQuery("SELECT m FROM Material m " +
                "WHERE UPPER(m.nombre) LIKE UPPER(:nombre) " +
                "ORDER BY m.codigo ASC");
        query.setParameter("nombre", "%" + filter + "%");
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public List<Material> findByTipoMaterial(Long idTipoMaterial) {
        return em.createQuery("SELECT m FROM Material m " +
                "WHERE m.tipoMaterial.id = :idtm " +
                "ORDER BY m.codigo ASC, m.nombre ASC ")
                .setParameter("idtm", idTipoMaterial)
                .getResultList();
    }
}
