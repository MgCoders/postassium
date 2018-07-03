package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.Cliente;
import coop.magnesium.potassium.db.entities.Equipo;
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
        return (List<Trabajo>) findByField("estado", estado);

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

    public List<Trabajo> findAllByEquipo(Equipo equipo){
        CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root entity = criteriaQuery.from(Trabajo.class);
        criteriaQuery.select(entity);
        criteriaQuery.where(criteriaBuilder.equal(entity.get("equipo"), criteriaBuilder.parameter(Equipo.class, "e")));
        Query query = this.getEntityManager().createQuery(criteriaQuery);
        query.setParameter("e", equipo);
        return (List<Trabajo>) query.getResultList();
    }



}
