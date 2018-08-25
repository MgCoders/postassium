package coop.magnesium.potassium.db.entities;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@JsonAutoDetect
@ApiModel
public class NucleoMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String codigo;

    @NotNull
    @ManyToOne
    private TipoMaterial tipoMaterial;

    public NucleoMaterial() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public TipoMaterial getTipoMaterial() {
        return tipoMaterial;
    }

    public void setTipoMaterial(TipoMaterial tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }
}
