package coop.magnesium.potassium.db.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;


/**
 * Created by msteglich on 1/20/18.
 */
@Entity
public class TipoEquipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipoEquipo;

    @NotNull
    private String descripcion;

    @NotNull
    private String dibujo; //Array de bytes del dibujo

    public TipoEquipo() {
    }

    public TipoEquipo(String descripcion, String dibujo) {
        this.descripcion = descripcion;
        this.dibujo = dibujo;
    }

    public Long getIdTipoEquipo() {
        return idTipoEquipo;
    }

    public void setIdTipoEquipo(Long idTipoEquipo) {
        this.idTipoEquipo = idTipoEquipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDibujo() {
        return dibujo;
    }

    public void setDibujo(String dibujo) {
        this.dibujo = dibujo;
    }
}
