package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.Rubro;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
