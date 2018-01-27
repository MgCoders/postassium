package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.Cliente;
import coop.magnesium.potassium.db.entities.Estado;
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
 * Created by msteglich on 1/23/18.
 */
@Stateless
public class TrabajoDao extends AbstractDao<Trabajo, Long>{

    @PersistenceContext
    EntityManager em;

    @Override
    public Class<Trabajo> getEntityClass() {
        return Trabajo.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Trabajo> findByEstado(String estado){
        List<Trabajo> result = findByField("estado", estado);
        if (result.size() == 0) return null;

        return result;

    }

    public List<Trabajo> findAllByCliente(Cliente cliente){
        CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root entity = criteriaQuery.from(Trabajo.class);
        criteriaQuery.select(entity);
        criteriaQuery.where(criteriaBuilder.equal(entity.get("cliente"), criteriaBuilder.parameter(Cliente.class, "c")));
        Query query = this.getEntityManager().createQuery(criteriaQuery);
        query.setParameter("c", cliente);
        return (List<Trabajo>) query.getResultList();
    }



}
