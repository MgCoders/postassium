package coop.magnesium.potassium.db.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


/**
 * Created by msteglich on 1/20/18.
 */
@Entity
@JsonAutoDetect
@ApiModel
@Table(name = "tipo_equipo")
public class TipoEquipo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idTipoEquipo;

    @NotNull
    @Column(unique = true)
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

    @Override
    public String toString() {
        return "TipoTarea{" +
                "id=" + idTipoEquipo +
                ", descripcion='" + descripcion + '\'' +
                ", dibujo='" + dibujo + '\'' +
                '}';
    }
}
