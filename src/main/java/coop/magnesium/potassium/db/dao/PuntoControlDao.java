package coop.magnesium.potassium.db.dao;


import coop.magnesium.potassium.db.entities.PuntoControl;
import coop.magnesium.potassium.db.entities.Trabajo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by msteglich on 1/31/18.
 */
@Stateless
public class PuntoControlDao extends AbstractDao<PuntoControl, Long> {


    @PersistenceContext
    EntityManager em;

    @Override
    public Class<PuntoControl> getEntityClass() {
        return PuntoControl.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<PuntoControl> findAllByTrabajo(Trabajo trabajo){
        CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root entity = criteriaQuery.from(PuntoControl.class);
        criteriaQuery.select(entity);
        criteriaQuery.where(criteriaBuilder.equal(entity.get("trabajo"), criteriaBuilder.parameter(Trabajo.class, "t")));
        Query query = this.getEntityManager().createQuery(criteriaQuery);
        query.setParameter("t", trabajo);
        return (List<PuntoControl>) query.getResultList();
    }

}