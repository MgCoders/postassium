package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.NotificacionDestinatario;
import coop.magnesium.potassium.db.entities.TipoNotificacion;
import coop.magnesium.potassium.db.entities.Usuario;

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
public class NotificacionDestinatarioDao extends AbstractDao<NotificacionDestinatario, Long> {

    @PersistenceContext
    EntityManager em;

    @Override
    public Class<NotificacionDestinatario> getEntityClass() {
        return NotificacionDestinatario.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public NotificacionDestinatario findByTipoAndUsuario(TipoNotificacion tipo, Usuario usuario) {
        CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root entity = criteriaQuery.from(NotificacionDestinatario.class);
        criteriaQuery.select(entity);

        Predicate tipoIsOk = criteriaBuilder.equal(entity.get("tipo"),
                criteriaBuilder.parameter(NotificacionDestinatario.class, "tipo"));
        Predicate usuarioIsOk = criteriaBuilder.equal(entity.get("usuario"),
                criteriaBuilder.parameter(Usuario.class, "usuario"));

        criteriaQuery.where(criteriaBuilder.and(tipoIsOk, usuarioIsOk));
        Query query = this.getEntityManager().createQuery(criteriaQuery);
        query.setParameter("tipo", tipo);
        query.setParameter("usuario", usuario);
        List<NotificacionDestinatario> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public List<NotificacionDestinatario> findAllByTipo(TipoNotificacion tipo) {
        CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root entity = criteriaQuery.from(NotificacionDestinatario.class);
        criteriaQuery.select(entity);

        Predicate tipoIsOk = criteriaBuilder.equal(entity.get("tipo"),
                criteriaBuilder.parameter(NotificacionDestinatario.class, "tipo"));

        criteriaQuery.where(tipoIsOk);
        Query query = this.getEntityManager().createQuery(criteriaQuery);
        query.setParameter("tipo", tipo);
        return query.getResultList();
    }
}
