package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.Material;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by pablo on 21/01/18.
 */
@Stateless
public class MaterialDao extends AbstractDao<Material, Long> {

    @PersistenceContext
    EntityManager em;

    @Override
    public Class<Material> getEntityClass() {
        return Material.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
