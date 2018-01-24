package coop.magnesium.potassium.db.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by msteglich on 1/20/18.
 */
@Entity
public class Equipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEquipo;

    @ManyToOne
    private Cliente cliente;

    @NotNull
    private String marca;

    @NotNull
    private String modelo;

    @NotNull
    private String matricula;

    @NotNull
    private String color;

    @ManyToOne
    private TipoEquipo tipoEquipo;

    public Equipo(Cliente cliente, String marca, String modelo, String matricula, String color, TipoEquipo tipoEquipo) {
        this.cliente = cliente;
        this.marca = marca;
        this.modelo = modelo;
        this.matricula = matricula;
        this.color = color;
        this.tipoEquipo = tipoEquipo;
    }

    public Long getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Long idEquipo) {
        this.idEquipo = idEquipo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
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
}
