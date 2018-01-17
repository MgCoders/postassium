package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.Cargo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by rsperoni on 28/10/17.
 */
@Stateless
public class CargoDao extends AbstractDao<Cargo, Long> {

    @PersistenceContext
    EntityManager em;

    @Override
    public Class<Cargo> getEntityClass() {
        return Cargo.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
