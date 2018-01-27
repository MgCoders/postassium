package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.Cliente;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by msteglich on 1/27/18.
 */
@Stateless
public class ClienteDao extends AbstractDao<Cliente, Long>{

    @PersistenceContext
    EntityManager em;

    @Override
    public Class<Cliente> getEntityClass() {
            return Cliente.class;
        }

    @Override
    public EntityManager getEntityManager() {
            return em;
            }

    public Cliente findByNombreEmpresa(String nombre){
            List<Cliente> result = findByField("nombreEmpresa", nombre);
            if (result.size() == 0) return null;

            return result.get(0);

    }



}
