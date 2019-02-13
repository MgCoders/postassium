package coop.magnesium.potassium.db.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by pablo on 23/01/18.
 */
@Entity
@JsonAutoDetect
@ApiModel
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"tarea_id", "material_id"}))
public class TareaMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "tarea_id", referencedColumnName = "id", nullable = false)
    private Tarea tarea;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "material_id", referencedColumnName = "id", nullable = false)
    private Material material;

    @ManyToOne
    @JoinColumn(name = "unidadmedida_id", referencedColumnName = "id", nullable = true)
    private UnidadMedida unidadMedida;

    public TareaMaterial() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }
}
