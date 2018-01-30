package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.Cliente;
import coop.magnesium.potassium.db.entities.Equipo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by msteglich on 1/28/18.
 */
@Stateless
public class EquipoDao extends AbstractDao<Equipo, Long>{

        @PersistenceContext
        EntityManager em;

        @Override
        public Class<Equipo> getEntityClass() {
            return Equipo.class;
        }

        @Override
        public EntityManager getEntityManager() {
            return em;
        }



        public List<Equipo> findAllByCliente(Cliente cliente){
            CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
            Root entity = criteriaQuery.from(Equipo.class);
            criteriaQuery.select(entity);
            criteriaQuery.where(criteriaBuilder.equal(entity.get("cliente"), criteriaBuilder.parameter(Cliente.class, "c")));
            Query query = this.getEntityManager().createQuery(criteriaQuery);
            query.setParameter("c", cliente);
            return (List<Equipo>) query.getResultList();
        }

        public Equipo findByMatricula(String matricula){
            List<Equipo> result = findByField("matricula", matricula);
            if (result.size() == 0) return null;

            return result.get(0);

        }
}
