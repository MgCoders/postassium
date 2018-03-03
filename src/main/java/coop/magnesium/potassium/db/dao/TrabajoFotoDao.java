package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.TrabajoFoto;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class TrabajoFotoDao extends AbstractDao<TrabajoFoto, Long> {


    @PersistenceContext
    EntityManager em;

    @Override
    public Class<TrabajoFoto> getEntityClass() {
        return TrabajoFoto.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return this.em;
    }
}
