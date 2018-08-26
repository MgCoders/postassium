package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.MarcaEquipo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by msteglich on 8/24/18.
 */
@Stateless
public class MarcaEquipoDao extends AbstractDao<MarcaEquipo, Long>{

    @PersistenceContext
            EntityManager em;

    @Override
    public Class<MarcaEquipo> getEntityClass() {
        return MarcaEquipo.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }



}
