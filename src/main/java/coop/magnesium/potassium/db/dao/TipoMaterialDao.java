package coop.magnesium.potassium.db.dao;

import coop.magnesium.potassium.db.entities.TipoMaterial;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class TipoMaterialDao extends AbstractDao<TipoMaterial, Long> {

    @PersistenceContext
    EntityManager em;

    @Override
    public Class<TipoMaterial> getEntityClass() {
        return TipoMaterial.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public TipoMaterial findByCodigo(String codigo) {
        List<TipoMaterial> tipoMaterialList = this.findByField("codigo", codigo);
        if (tipoMaterialList.isEmpty()) {
            return null;
        }
        return tipoMaterialList.get(0); // constraint unique por codigo. Es cero o uno la lista.
    }
}
