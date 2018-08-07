package coop.magnesium.potassium.db.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by msteglich on 1/31/18.
 */
@Entity
@JsonAutoDetect
@ApiModel
public class PuntoControl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    @ManyToOne
    private Trabajo trabajo;

    @ManyToOne
    private Usuario responsable;

    @NotNull
    private Integer orden;

    @JsonIgnore
    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "puntoControl")
    private List<Tarea> tareas = new ArrayList<>();


    private Boolean verificado;

    @NotNull
    private Boolean paraVerificar = false;

    public PuntoControl() {
    }

    public PuntoControl(String nombre, Trabajo trabajo, Integer orden, Boolean paraVerificar) {
        this.nombre = nombre;
        this.trabajo = trabajo;
        this.orden = orden;
        this.paraVerificar = paraVerificar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Trabajo getTrabajo() {
        return trabajo;
    }

    public void setTrabajo(Trabajo trabajo) {
        this.trabajo = trabajo;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuario responsable) {
        this.responsable = responsable;
    }

    public List<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }

    public Boolean getVerificado() {
        return verificado;
    }

    public void setVerificado(Boolean verificado) {
        this.verificado = verificado;
    }

    public Boolean getParaVerificar() {
        return paraVerificar;
    }

    public void setParaVerificar(Boolean paraVerificar) {
        this.paraVerificar = paraVerificar;
    }
}
