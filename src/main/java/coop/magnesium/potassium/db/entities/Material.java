package coop.magnesium.potassium.db.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by pablo on 21/01/18.
 */
@Entity
@JsonAutoDetect
@ApiModel
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(unique = true)
    private String codigo;
    @NotNull(message = "Nombre no puede ser vacío")
    private String nombre;
    private String descripcion;
    private String codigoAlternativo;
    private Boolean stock;

    @ManyToOne
    private UnidadMedida medida1;

    @ManyToOne
    private UnidadMedida medida2;
    private Double factorConversion2;

    @ManyToOne
    private UnidadMedida medida3;
    private Double factorConversion3;

    @NotNull
    @ManyToOne
    private TipoMaterial tipoMaterial;

    @ManyToOne
    private NucleoMaterial nucleoMaterial;

    public Material() {
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

    public String getCodigoAlternativo() {
        return codigoAlternativo;
    }

    public void setCodigoAlternativo(String codigoAlternativo) {
        this.codigoAlternativo = codigoAlternativo;
    }

    public Boolean getStock() {
        return stock;
    }

    public void setStock(Boolean stock) {
        this.stock = stock;
    }

    public UnidadMedida getMedida1() {
        return medida1;
    }

    public void setMedida1(UnidadMedida medida1) {
        this.medida1 = medida1;
    }

    public UnidadMedida getMedida2() {
        return medida2;
    }

    public void setMedida2(UnidadMedida medida2) {
        this.medida2 = medida2;
    }

    public Double getFactorConversion2() {
        return factorConversion2;
    }

    public void setFactorConversion2(Double factorConversion2) {
        this.factorConversion2 = factorConversion2;
    }

    public UnidadMedida getMedida3() {
        return medida3;
    }

    public void setMedida3(UnidadMedida medida3) {
        this.medida3 = medida3;
    }

    public Double getFactorConversion3() {
        return factorConversion3;
    }

    public void setFactorConversion3(Double factorConversion3) {
        this.factorConversion3 = factorConversion3;
    }

    public TipoMaterial getTipoMaterial() {
        return tipoMaterial;
    }

    public void setTipoMaterial(TipoMaterial tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }

    public NucleoMaterial getNucleoMaterial() {
        return nucleoMaterial;
    }

    public void setNucleoMaterial(NucleoMaterial nucleoMaterial) {
        this.nucleoMaterial = nucleoMaterial;
    }
}
