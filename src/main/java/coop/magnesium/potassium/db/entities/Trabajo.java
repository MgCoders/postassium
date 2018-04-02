package coop.magnesium.potassium.db.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by msteglich on 1/20/18.
 */
@Entity
public class Trabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    private Boolean equipoDocumentos;

    private Boolean equipoRadio;

    private Boolean equipoExtintor;

    private Boolean equipoBalizas;

    private Boolean equipoLlaveRuedas;

    private Boolean equipoHerramientas;

    private Boolean equipoManuales;

    private Boolean equipoFrenteRadio;

    private Boolean equipoMangueraCabina;

    private Boolean equipoCenicero;

    private Boolean equipoGatoPalanca;

    private Boolean equipoParabrisasSano;

    private Boolean equipoVidriosLaterales;

    private Boolean equipoVidriosLateralesSanos;

    private Boolean equipoEspejos;

    private Boolean equipoEspejosSanos;

    private Boolean equipoSenalerosSanos;

    private Boolean equipoLucesTraserasSanas;

    private Boolean equipoRayones;

    private Boolean equipoAbollones;

    private Boolean equipoAuxiliar;

    private Boolean equipoAuxiliarArmada;

    private Integer equipoCantidadCombustible;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getEquipoDocumentos() {
        return equipoDocumentos;
    }

    public void setEquipoDocumentos(Boolean equipoDocumentos) {
        this.equipoDocumentos = equipoDocumentos;
    }

    public Boolean getEquipoRadio() {
        return equipoRadio;
    }

    public void setEquipoRadio(Boolean equipoRadio) {
        this.equipoRadio = equipoRadio;
    }

    public Boolean getEquipoExtintor() {
        return equipoExtintor;
    }

    public void setEquipoExtintor(Boolean equipoExtintor) {
        this.equipoExtintor = equipoExtintor;
    }

    public Boolean getEquipoBalizas() {
        return equipoBalizas;
    }

    public void setEquipoBalizas(Boolean equipoBalizas) {
        this.equipoBalizas = equipoBalizas;
    }

    public Boolean getEquipoLlaveRuedas() {
        return equipoLlaveRuedas;
    }

    public void setEquipoLlaveRuedas(Boolean equipoLlaveRuedas) {
        this.equipoLlaveRuedas = equipoLlaveRuedas;
    }

    public Boolean getEquipoHerramientas() {
        return equipoHerramientas;
    }

    public void setEquipoHerramientas(Boolean equipoHerramientas) {
        this.equipoHerramientas = equipoHerramientas;
    }

    public Boolean getEquipoManuales() {
        return equipoManuales;
    }

    public void setEquipoManuales(Boolean equipoManuales) {
        this.equipoManuales = equipoManuales;
    }

    public Boolean getEquipoFrenteRadio() {
        return equipoFrenteRadio;
    }

    public void setEquipoFrenteRadio(Boolean equipoFrenteRadio) {
        this.equipoFrenteRadio = equipoFrenteRadio;
    }

    public Boolean getEquipoMangueraCabina() {
        return equipoMangueraCabina;
    }

    public void setEquipoMangueraCabina(Boolean equipoMangueraCabina) {
        this.equipoMangueraCabina = equipoMangueraCabina;
    }

    public Boolean getEquipoCenicero() {
        return equipoCenicero;
    }

    public void setEquipoCenicero(Boolean equipoCenicero) {
        this.equipoCenicero = equipoCenicero;
    }

    public Boolean getEquipoGatoPalanca() {
        return equipoGatoPalanca;
    }

    public void setEquipoGatoPalanca(Boolean equipoGatoPalanca) {
        this.equipoGatoPalanca = equipoGatoPalanca;
    }

    public Boolean getEquipoParabrisasSano() {
        return equipoParabrisasSano;
    }

    public void setEquipoParabrisasSano(Boolean equipoParabrisasSano) {
        this.equipoParabrisasSano = equipoParabrisasSano;
    }

    public Boolean getEquipoVidriosLaterales() {
        return equipoVidriosLaterales;
    }

    public void setEquipoVidriosLaterales(Boolean equipoVidriosLaterales) {
        this.equipoVidriosLaterales = equipoVidriosLaterales;
    }

    public Boolean getEquipoVidriosLateralesSanos() {
        return equipoVidriosLateralesSanos;
    }

    public void setEquipoVidriosLateralesSanos(Boolean equipoVidriosLateralesSanos) {
        this.equipoVidriosLateralesSanos = equipoVidriosLateralesSanos;
    }

    public Boolean getEquipoEspejos() {
        return equipoEspejos;
    }

    public void setEquipoEspejos(Boolean equipoEspejos) {
        this.equipoEspejos = equipoEspejos;
    }

    public Boolean getEquipoEspejosSanos() {
        return equipoEspejosSanos;
    }

    public void setEquipoEspejosSanos(Boolean equipoEspejosSanos) {
        this.equipoEspejosSanos = equipoEspejosSanos;
    }

    public Boolean getEquipoSenalerosSanos() {
        return equipoSenalerosSanos;
    }

    public void setEquipoSenalerosSanos(Boolean equipoSenalerosSanos) {
        this.equipoSenalerosSanos = equipoSenalerosSanos;
    }

    public Boolean getEquipoLucesTraserasSanas() {
        return equipoLucesTraserasSanas;
    }

    public void setEquipoLucesTraserasSanas(Boolean equipoLucesTraserasSanas) {
        this.equipoLucesTraserasSanas = equipoLucesTraserasSanas;
    }

    public Boolean getEquipoRayones() {
        return equipoRayones;
    }

    public void setEquipoRayones(Boolean equipoRayones) {
        this.equipoRayones = equipoRayones;
    }

    public Boolean getEquipoAbollones() {
        return equipoAbollones;
    }

    public void setEquipoAbollones(Boolean equipoAbollones) {
        this.equipoAbollones = equipoAbollones;
    }

    public Boolean getEquipoAuxiliar() {
        return equipoAuxiliar;
    }

    public void setEquipoAuxiliar(Boolean equipoAuxiliar) {
        this.equipoAuxiliar = equipoAuxiliar;
    }

    public Boolean getEquipoAuxiliarArmada() {
        return equipoAuxiliarArmada;
    }

    public void setEquipoAuxiliarArmada(Boolean equipoAuxiliarArmada) {
        this.equipoAuxiliarArmada = equipoAuxiliarArmada;
    }

    public Integer getEquipoCantidadCombustible() {
        return equipoCantidadCombustible;
    }

    public void setEquipoCantidadCombustible(Integer equipoCantidadCombustible) {
        this.equipoCantidadCombustible = equipoCantidadCombustible;
    }
}
