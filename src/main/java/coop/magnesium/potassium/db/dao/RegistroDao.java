package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.Registro;
import coop.magnesium.potassium.db.entities.Tarea;

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
 * Created by pablo on 25/01/18.
 */
@Stateless
public class RegistroDao extends AbstractDao<Registro, Long> {

    @PersistenceContext
    EntityManager em;

    @Override
    public Class<Registro> getEntityClass() {
        return Registro.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Registro> findAllByTarea(Tarea tarea) {
        CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root entity = criteriaQuery.from(Registro.class);
        criteriaQuery.select(entity);

        Predicate tareaIsOk = criteriaBuilder
                .equal(entity.get("tarea").get("id"), criteriaBuilder.parameter(Long.class,"tarea_id"));
        Predicate notDeleted = criteriaBuilder
                .equal(entity.get("borrado"), criteriaBuilder.parameter(Integer.class,"d"));

        criteriaQuery.where(criteriaBuilder.and(tareaIsOk, notDeleted));
        criteriaQuery.orderBy(criteriaBuilder.asc(entity.get("fecha")));
        Query query = em.createQuery(criteriaQuery);
        query.setParameter("tarea_id", tarea.getId());
        query.setParameter("d", 0);
        return query.getResultList();
    }

}
