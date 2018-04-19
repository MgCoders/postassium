package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.Equipo;
import coop.magnesium.potassium.db.entities.Factura;
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
 * Created by msteglich on 4/13/18.
 */
@Stateless
public class FacturaDao extends AbstractDao<Factura, Long>{

    @PersistenceContext
    EntityManager em;

    @Override
    public Class<Factura> getEntityClass() {
        return Factura.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }



    public Factura findByTrabajo(Trabajo trabajo)
    {
        CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root entity = criteriaQuery.from(Equipo.class);
        criteriaQuery.select(entity);
        criteriaQuery.where(criteriaBuilder.equal(entity.get("trabajo"), criteriaBuilder.parameter(Trabajo.class, "t")));
        Query query = this.getEntityManager().createQuery(criteriaQuery);
        query.setParameter("t", trabajo);
        return query.getResultList().isEmpty() ? null : (Factura) query.getResultList().get(0);
    }


}
