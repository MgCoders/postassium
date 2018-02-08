package coop.magnesium.potassium.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by pablo on 03/02/18.
 */
@MappedSuperclass
public class AbstractEntity implements Serializable {

    @JsonIgnore
    @NotNull
    protected int borrado = 0;
//
//    @JsonIgnore
//    protected LocalDateTime creado;
//
//    @JsonIgnore
//    protected LocalDateTime modificado;
//
//    @PreUpdate
//    protected void commonPreUpdate() {
//        if (creado == null) creado = LocalDateTime.now();
//        modificado = LocalDateTime.now();
//    }

    public int getBorrado() {
        return borrado;
    }

    public void setBorrado(int borrado) {
        this.borrado = borrado;
    }

//
//    public LocalDateTime getCreado() {
//        return creado;
//    }
//
//    public void setCreado(LocalDateTime creado) {
//        this.creado = creado;
//    }
//
//    public LocalDateTime getModificado() {
//        return modificado;
//    }
//
//    public void setModificado(LocalDateTime modificado) {
//        this.modificado = modificado;
//    }
}
