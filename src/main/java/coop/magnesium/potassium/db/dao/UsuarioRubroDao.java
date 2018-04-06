package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.Rubro;
import coop.magnesium.potassium.db.entities.Usuario;
import coop.magnesium.potassium.db.entities.UsuarioRubro;

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
public class UsuarioRubroDao extends AbstractDao<UsuarioRubro, Long> {

    @PersistenceContext
    EntityManager em;

    @Override
    public Class<UsuarioRubro> getEntityClass() {
        return UsuarioRubro.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return this.em;
    }

    public UsuarioRubro findByUsuarioAndRubro(Usuario usuario, Rubro rubro) {
        CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root entity = criteriaQuery.from(UsuarioRubro.class);
        criteriaQuery.select(entity);

        Predicate usuarioIsOk = criteriaBuilder
                .equal(entity.get("usuario").get("id"), criteriaBuilder.parameter(Long.class,"usuario_id"));
        Predicate rubroIsOk = criteriaBuilder
                .equal(entity.get("rubro").get("id"), criteriaBuilder.parameter(Long.class,"rubro_id"));

        criteriaQuery.where(criteriaBuilder.and(usuarioIsOk, rubroIsOk));
        Query query = em.createQuery(criteriaQuery);
        query.setParameter("usuario_id", usuario.getId());
        query.setParameter("rubro_id", rubro.getId());

        List<UsuarioRubro> result = query.getResultList();
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }
}
