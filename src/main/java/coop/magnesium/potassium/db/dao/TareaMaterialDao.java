package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.Material;
import coop.magnesium.potassium.db.entities.Tarea;
import coop.magnesium.potassium.db.entities.TareaMaterial;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
public class TareaMaterialDao extends AbstractDao<TareaMaterial, Long> {

    @PersistenceContext
    EntityManager em;

    @Override
    public Class<TareaMaterial> getEntityClass() {
        return TareaMaterial.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }


    public List<TareaMaterial> findAllByTarea(Tarea tarea) {
        CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root entity = criteriaQuery.from(TareaMaterial.class);
        criteriaQuery.select(entity);

        Predicate tareaIsOk = criteriaBuilder
                .equal(entity.get("tarea").get("id"), criteriaBuilder.parameter(Long.class,"tarea_id"));

        criteriaQuery.where(tareaIsOk);
        Query query = em.createQuery(criteriaQuery);
        query.setParameter("tarea_id", tarea.getId());
        return query.getResultList();
    }

    public TareaMaterial findByTareaAndMaterial(Tarea tarea, Material material) {
        CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root entity = criteriaQuery.from(TareaMaterial.class);
        criteriaQuery.select(entity);

        Predicate tareaIsOk = criteriaBuilder
                .equal(entity.get("tarea").get("id"), criteriaBuilder.parameter(Long.class,"tarea_id"));
        Predicate materialIsOk = criteriaBuilder
                .equal(entity.get("material").get("id"), criteriaBuilder.parameter(Long.class,"material_id"));

        criteriaQuery.where(criteriaBuilder.and(tareaIsOk, materialIsOk));
        Query query = em.createQuery(criteriaQuery);
        query.setParameter("tarea_id", tarea.getId());
        query.setParameter("material_id", material.getId());
        List<TareaMaterial> result = query.getResultList();
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }
}
