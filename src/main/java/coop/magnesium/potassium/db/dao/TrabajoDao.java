package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.Cliente;
import coop.magnesium.potassium.db.entities.Equipo;
import coop.magnesium.potassium.db.entities.Estado;
import coop.magnesium.potassium.db.entities.Trabajo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.Arrays;
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

    public List<Trabajo> findAllNearDeadline() {
        LocalDate hoy = LocalDate.now();
        LocalDate deadline = hoy.plusDays(6);

        CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root entity = criteriaQuery.from(Trabajo.class);
        criteriaQuery.select(entity);

        Predicate start = criteriaBuilder.greaterThanOrEqualTo(entity.get("fechaProvistaEntrega"), hoy);
        Predicate end = criteriaBuilder.lessThanOrEqualTo(entity.get("fechaProvistaEntrega"), deadline);

        List<String> sinTerminar = Arrays.asList(
                new String[]{Estado.EN_PROCESO.toString() });
        Expression<String> estadosExp = entity.get("estadp");
        Predicate estados = estadosExp.in(sinTerminar);
        criteriaQuery.where(criteriaBuilder.and(start, end, estados));

        Query query = this.getEntityManager().createQuery(criteriaQuery);
        return query.getResultList();
    }

    public List<Trabajo> findAllDelayedWorks() {
        LocalDate hoy = LocalDate.now();

        CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root entity = criteriaQuery.from(Trabajo.class);
        criteriaQuery.select(entity);
        Predicate end = criteriaBuilder.lessThan(entity.get("fechaProvistaEntrega"), hoy);

        List<String> sinTerminar = Arrays.asList(
                new String[]{Estado.EN_PROCESO.toString() });
        Expression<String> estadosExp = entity.get("estadp");
        Predicate estados = estadosExp.in(sinTerminar);
        criteriaQuery.where(criteriaBuilder.and(end, estados));

        Query query = this.getEntityManager().createQuery(criteriaQuery);
        return query.getResultList();
    }

}
