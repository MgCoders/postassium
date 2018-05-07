package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.TrabajoFoto;

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
public class TrabajoFotoDao extends AbstractDao<TrabajoFoto, Long> {


    @PersistenceContext
    EntityManager em;

    @Override
    public Class<TrabajoFoto> getEntityClass() {
        return TrabajoFoto.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return this.em;
    }


    public List<TrabajoFoto> findAllByTrabajo(Long trabajoId) {
        CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root entity = criteriaQuery.from(TrabajoFoto.class);
        criteriaQuery.select(entity);

        Predicate tareaIsOk = criteriaBuilder
                .equal(entity.get("trabajo").get("id"),
                        criteriaBuilder.parameter(Long.class,"trabajo_id"));

        criteriaQuery.where(tareaIsOk);
        Query query = em.createQuery(criteriaQuery);
        query.setParameter("trabajo_id", trabajoId);
        return query.getResultList();
    }
}
