package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.Usuario;
import coop.magnesium.potassium.utils.ex.MagnesiumBdMultipleResultsException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by rsperoni on 28/10/17.
 */
@Stateless
public class UsuarioDao extends AbstractDao<Usuario, Long> {

    @PersistenceContext
    EntityManager em;

    @Override
    public Class<Usuario> getEntityClass() {
        return Usuario.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Usuario findByEmail(String email) throws MagnesiumBdMultipleResultsException {
        List<Usuario> result = findByField("email", email);
        if (result.size() == 0) return null;
        if (result.size() > 1)
            throw new MagnesiumBdMultipleResultsException(Usuario.class.getSimpleName() + "multiples resultados encontrados");
        return result.get(0);
    }
}
