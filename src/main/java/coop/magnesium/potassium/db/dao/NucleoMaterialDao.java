package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.NucleoMaterial;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class NucleoMaterialDao extends AbstractDao<NucleoMaterial, Long> {


    @PersistenceContext
    EntityManager em;

    @Override
    public Class<NucleoMaterial> getEntityClass() {
        return NucleoMaterial.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<NucleoMaterial> findAllByTipoMaterial(Long id) {
        Query query = em.createQuery("SELECT n FROM NucleoMaterial n " +
                "WHERE n.tipoMaterial.id = :idtp");
        query.setParameter("idtp", id);
        return query.getResultList();
    }
}
