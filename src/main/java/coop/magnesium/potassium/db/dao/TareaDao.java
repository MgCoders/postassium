package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.Tarea;
import coop.magnesium.potassium.db.entities.Trabajo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by pablo on 21/01/18.
 */
@Stateless
public class TareaDao extends AbstractDao<Tarea, Long> {


    @PersistenceContext
    EntityManager em;

    @Override
    public Class<Tarea> getEntityClass() {
        return Tarea.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Tarea> findAllByTrabajo(Long trabajoId) {
        CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root entity = criteriaQuery.from(Tarea.class);
        criteriaQuery.select(entity);

        Predicate tareaIsOk = criteriaBuilder
                .equal(entity.get("puntoControl").get("trabajo").get("id"),
                        criteriaBuilder.parameter(Long.class,"trabajo_id"));

        criteriaQuery.where(tareaIsOk);
        criteriaQuery.orderBy(criteriaBuilder.asc(entity.get("puntoControl").get("id")));
        Query query = em.createQuery(criteriaQuery);
        query.setParameter("trabajo_id", trabajoId);
        return query.getResultList();
    }
}
