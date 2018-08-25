package coop.magnesium.potassium.db.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by pablo on 23/01/18.
 */
@Entity
@JsonAutoDetect
@ApiModel
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"familia", "grupo", "subgrupo"}))
public class TipoMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String familia;

    @NotNull
    private String grupo;

    @NotNull
    private String subgrupo;

    private String descripcion;

    public TipoMaterial() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String nombre) {
        this.familia = nombre;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String codigo) {
        this.grupo = codigo;
    }

    public String getSubgrupo() {
        return subgrupo;
    }

    public void setSubgrupo(String subgrupo) {
        this.subgrupo = subgrupo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
