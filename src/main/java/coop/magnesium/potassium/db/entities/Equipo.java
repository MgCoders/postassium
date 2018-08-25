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
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.ALL})
    @NotNull(message = "El equipo debe tener un Cliente")
    private Cliente cliente;

    @ManyToOne(cascade = {CascadeType.ALL})
    @NotNull(message = "El equipo debe tener una marca")
    private MarcaEquipo marca;

    private String modelo;

    private String matricula;

    private String numeroChasis;

    private String color;

    @NotNull(message = "El equipo debe tener una descripcion")
    private String descripcion;

    @NotNull(message = "El equipo debe tener un tipo de equipo")
    @ManyToOne(cascade = {CascadeType.ALL})
    private TipoEquipo tipoEquipo;


    public Equipo() {
    }

    public Equipo(Cliente cliente, MarcaEquipo marca, String modelo, String matricula, String color, TipoEquipo tipoEquipo) {
        this.cliente = cliente;
        this.marca = marca;
        this.modelo = modelo;
        this.matricula = matricula;
        this.color = color;
        this.tipoEquipo = tipoEquipo;
    }

    public Equipo(Cliente cliente, MarcaEquipo marca, String modelo, String matricula, String numeroChasis, String color, TipoEquipo tipoEquipo) {
        this.cliente = cliente;
        this.marca = marca;
        this.modelo = modelo;
        this.matricula = matricula;
        this.numeroChasis = numeroChasis;
        this.color = color;
        this.tipoEquipo = tipoEquipo;
    }

    public Equipo(Cliente cliente, MarcaEquipo marca, String modelo, String matricula, String numeroChasis, String color, String descripcion, TipoEquipo tipoEquipo) {
        this.cliente = cliente;
        this.marca = marca;
        this.modelo = modelo;
        this.matricula = matricula;
        this.numeroChasis = numeroChasis;
        this.color = color;
        this.descripcion = descripcion;
        this.tipoEquipo = tipoEquipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public MarcaEquipo getMarca() {
        return marca;
    }

    public void setMarca(MarcaEquipo marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public TipoEquipo getTipoEquipo() {
        return tipoEquipo;
    }

    public void setTipoEquipo(TipoEquipo tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
    }

    public String getNumeroChasis() {
        return numeroChasis;
    }

    public void setNumeroChasis(String numeroChasis) {
        this.numeroChasis = numeroChasis;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
