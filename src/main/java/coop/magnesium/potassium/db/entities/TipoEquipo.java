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
    private Long id;

    @NotNull
    @Column(unique = true)
    private String descripcion;

    @NotNull
    @Column(length=10485760)
    private String dibujo; //Array de bytes del dibujo

    private Boolean requiereInfoRecibo;

    public TipoEquipo() {
    }

    public TipoEquipo(String descripcion, String dibujo, Boolean requiereInfoRecibo) {
        this.descripcion = descripcion;
        this.dibujo = dibujo;
        this.requiereInfoRecibo = requiereInfoRecibo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getRequiereInfoRecibo() {
        return requiereInfoRecibo;
    }

    public void setRequiereInfoRecibo(Boolean requiereInfoRecibo) {
        this.requiereInfoRecibo = requiereInfoRecibo;
    }

    @Override
    public String toString() {
        return "TipoEquipo{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", dibujo='" + dibujo + '\'' +
                '}';
    }
}
