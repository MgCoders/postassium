package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.Rubro;

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
public class RubroDao extends AbstractDao<Rubro, Long> {

    @PersistenceContext
    EntityManager em;

    @Override
    public Class<Rubro> getEntityClass() {
        return Rubro.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Rubro findByName(String name) {
        CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root entity = criteriaQuery.from(Rubro.class);
        criteriaQuery.select(entity);

        Predicate tareaIsOk = criteriaBuilder
                .equal(entity.get("nombre"), criteriaBuilder.parameter(String.class,"nombre"));
        Predicate notDeleted = criteriaBuilder
                .equal(entity.get("borrado"), criteriaBuilder.parameter(Integer.class,"d"));
        criteriaQuery.where(criteriaBuilder.and(tareaIsOk, notDeleted));

        Query query = em.createQuery(criteriaQuery);
        query.setParameter("nombre", name);
        query.setParameter("d", 0);
        List<Rubro> rubros = query.getResultList();
        return rubros.isEmpty() ? null : rubros.get(0);
    }
}
