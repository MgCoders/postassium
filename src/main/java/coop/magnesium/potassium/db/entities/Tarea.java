package coop.magnesium.potassium.db.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by pablo on 21/01/18.
 */
@Entity
@JsonAutoDetect
@ApiModel
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;

    @Column(name = "minutos_estimados")
    private Integer minutosEstimados;

    private Integer borrado = 0;


    public Tarea() {
    }

    public Tarea(String nombre, String descripcion, Integer minutosEstimados, Integer borrada) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.minutosEstimados = minutosEstimados;
        this.borrado = borrada;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getMinutosEstimados() {
        return minutosEstimados;
    }

    public void setMinutosEstimados(Integer minutosEstimados) {
        this.minutosEstimados = minutosEstimados;
    }

    public Integer getBorrado() {
        return borrado;
    }

    public void setBorrado(Integer borrado) {
        this.borrado = borrado;
    }
}
