package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.Registro;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by pablo on 25/01/18.
 */
@Stateless
public class RegistroDao extends AbstractDao<Registro, Long> {

    @PersistenceContext
    EntityManager em;

    @Override
    public Class<Registro> getEntityClass() {
        return Registro.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
