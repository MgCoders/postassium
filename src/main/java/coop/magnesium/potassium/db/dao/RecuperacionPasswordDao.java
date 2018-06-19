package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.RecuperacionPassword;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by rsperoni on 28/10/17.
 */
@Stateless
public class RecuperacionPasswordDao extends AbstractDao<RecuperacionPassword, String> {

    @PersistenceContext
    EntityManager em;

    @Override
    public Class<RecuperacionPassword> getEntityClass() {
        return RecuperacionPassword.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }


}
