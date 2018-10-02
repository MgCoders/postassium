package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.TipoMaterial;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class TipoMaterialDao extends AbstractDao<TipoMaterial, Long> {

    @PersistenceContext
    EntityManager em;

    @Override
    public Class<TipoMaterial> getEntityClass() {
        return TipoMaterial.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public TipoMaterial findByCodigo(String familia, String grupo, String subgrupo) {
        Query query = em.createQuery("SELECT ta " +
                "FROM TipoMaterial ta " +
                "WHERE ta.familia = :familia AND ta.grupo = :grupo AND ta.subgrupo = :subgrupo");
        query.setParameter("familia", familia);
        query.setParameter("grupo", grupo);
        query.setParameter("subgrupo", subgrupo);
        List<TipoMaterial> tipoMaterialList = query.getResultList();
        if (tipoMaterialList.isEmpty()) {
            return null;
        }
        return tipoMaterialList.get(0); // constraint unique por codigo. Es cero o uno la lista.
    }

    public List<TipoMaterial> findByPattern(String pattern) {
        Query query = em.createQuery("SELECT tm FROM TipoMaterial tm " +
                "WHERE UPPER(tm.familia) LIKE UPPER(:pattern) OR " +
                "UPPER(tm.grupo) LIKE UPPER(:pattern) OR " +
                "UPPER(tm.subgrupo) LIKE UPPER(:pattern)");
        query.setParameter("pattern", "%" + pattern + "%");
        return query.getResultList();
    }

}
