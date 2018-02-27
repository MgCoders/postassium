package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.TipoEquipo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class TipoEquioDao extends AbstractDao<TipoEquipo, Long> {
    @PersistenceContext
    EntityManager em;

    @Override
    public Class<TipoEquipo> getEntityClass() {
        return TipoEquipo.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
