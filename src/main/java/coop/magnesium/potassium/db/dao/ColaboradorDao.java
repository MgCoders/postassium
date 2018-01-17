package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.Colaborador;
import coop.magnesium.potassium.utils.ex.MagnesiumBdMultipleResultsException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by rsperoni on 28/10/17.
 */
@Stateless
public class ColaboradorDao extends AbstractDao<Colaborador, Long> {

    @PersistenceContext
    EntityManager em;

    @Override
    public Class<Colaborador> getEntityClass() {
        return Colaborador.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Colaborador findByEmail(String email) throws MagnesiumBdMultipleResultsException {
        List<Colaborador> result = findByField("email", email);
        if (result.size() == 0) return null;
        if (result.size() > 1)
            throw new MagnesiumBdMultipleResultsException(Colaborador.class.getSimpleName() + "multiples resultados encontrados");
        return result.get(0);
    }
}
