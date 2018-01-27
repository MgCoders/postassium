package coop.magnesium.potassium.db.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by msteglich on 1/20/18.
 */
@Entity
public class Trabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTrabajo;

    @NotNull
    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Equipo equipo;

    @NotNull
    private String motivoVisita;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    @ApiModelProperty(dataType = "date", example = "23-01-2017 17:20")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime fechaRecepcion;


    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @ApiModelProperty(dataType = "date", example = "23-01-2017")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate fechaProvistaEntrega;

    private Boolean requierePresupuesto;

    private String comentarios;

    @NotNull
    private String estado = Estado.CREADO.name();

    //@ManyToOne
    //private Usuario usuarioRecepcion;

    private String dibujoEquipoRecepcion; //Para el dibujito de cuando lo recibe, guardar el array de bytes


    private BigDecimal kmEquipoRecepcion;

    private String firmaClienteRecepcion; //Guardar array de bytes de la firma

    private String nombreClienteRecepcion;

    private Integer nroFactura;

    private Integer nroRemito;

    private Integer nroOrdenCompra;


    public Trabajo() {
    }

    public Trabajo(Cliente cliente) {
        this.cliente = cliente;
    }

    public Long getIdTrabajo() {
        return idTrabajo;
    }

    public void setIdTrabajo(Long idTrabajo) {
        this.idTrabajo = idTrabajo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaProvistaEntrega() {
        return fechaProvistaEntrega;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public String getMotivoVisita() {
        return motivoVisita;
    }

    public void setMotivoVisita(String motivoVisita) {
        this.motivoVisita = motivoVisita;
    }

    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public void setFechaProvistaEntrega(LocalDate fechaProvistaEntrega) {
        this.fechaProvistaEntrega = fechaProvistaEntrega;
    }

    public Boolean getRequierePresupuesto() {
        return requierePresupuesto;
    }

    public void setRequierePresupuesto(Boolean requierePresupuesto) {
        this.requierePresupuesto = requierePresupuesto;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }


    public String getDibujoEquipoRecepcion() {
        return dibujoEquipoRecepcion;
    }

    public void setDibujoEquipoRecepcion(String dibujoEquipoRecepcion) {
        this.dibujoEquipoRecepcion = dibujoEquipoRecepcion;
    }

    public BigDecimal getKmEquipoRecepcion() {
        return kmEquipoRecepcion;
    }

    public void setKmEquipoRecepcion(BigDecimal kmEquipoRecepcion) {
        this.kmEquipoRecepcion = kmEquipoRecepcion;
    }

    public String getFirmaClienteRecepcion() {
        return firmaClienteRecepcion;
    }

    public void setFirmaClienteRecepcion(String firmaClienteRecepcion) {
        this.firmaClienteRecepcion = firmaClienteRecepcion;
    }

    public String getNombreClienteRecepcion() {
        return nombreClienteRecepcion;
    }

    public void setNombreClienteRecepcion(String nombreClienteRecepcion) {
        this.nombreClienteRecepcion = nombreClienteRecepcion;
    }

    public Integer getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(Integer nroFactura) {
        this.nroFactura = nroFactura;
    }

    public Integer getNroRemito() {
        return nroRemito;
    }

    public void setNroRemito(Integer nroRemito) {
        this.nroRemito = nroRemito;
    }

    public Integer getNroOrdenCompra() {
        return nroOrdenCompra;
    }

    public void setNroOrdenCompra(Integer nroOrdenCompra) {
        this.nroOrdenCompra = nroOrdenCompra;
    }
}
