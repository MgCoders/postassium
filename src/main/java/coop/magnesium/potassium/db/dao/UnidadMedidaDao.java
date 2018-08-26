package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.UnidadMedida;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class UnidadMedidaDao extends AbstractDao<UnidadMedida, Long> {

    @PersistenceContext
    EntityManager em;

    @Override
    public Class<UnidadMedida> getEntityClass() {
        return UnidadMedida.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public UnidadMedida findByCodigo(String codigo) {
        List<UnidadMedida> unidadMedidas = this.findByField("codigo", codigo);
        if (unidadMedidas.size() == 0) {
            return null;
        }
        return unidadMedidas.get(0);
    }
}
