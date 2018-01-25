package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.Tarea;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by pablo on 21/01/18.
 */
@Stateless
public class TareaDao extends AbstractDao<Tarea, Long> {


    @PersistenceContext
    EntityManager em;

    @Override
    public Class<Tarea> getEntityClass() {
        return Tarea.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
