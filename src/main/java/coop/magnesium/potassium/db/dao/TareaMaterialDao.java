package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.Tarea;
import coop.magnesium.potassium.db.entities.TareaMaterial;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

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
        criteriaQuery.orderBy(criteriaBuilder.asc(entity.get("fecha")));
        Query query = em.createQuery(criteriaQuery);
        query.setParameter("tarea_id", tarea.getId());
        return query.getResultList();
    }
}
