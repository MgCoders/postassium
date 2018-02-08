package coop.magnesium.potassium.db.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by msteglich on 1/20/18.
 */
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nombreEmpresa;

    private String rut;

    private String telefono;

    @NotNull
    private String personaContacto;

    @NotNull
    private String telefonoContacto;

    private String emailEmpresa;

    private String direccion;

    public Cliente() {
    }

    public Cliente(String nombreEmpresa, String rut, String telefono, String personaContacto, String telefonoContacto, String emailEmpresa, String direccion) {
        this.nombreEmpresa = nombreEmpresa;
        this.rut = rut;
        this.telefono = telefono;
        this.personaContacto = personaContacto;
        this.telefonoContacto = telefonoContacto;
        this.emailEmpresa = emailEmpresa;
        this.direccion = direccion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPersonaContacto() {
        return personaContacto;
    }

    public void setPersonaContacto(String personaContacto) {
        this.personaContacto = personaContacto;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getEmailEmpresa() {
        return emailEmpresa;
    }

    public void setEmailEmpresa(String emailEmpresa) {
        this.emailEmpresa = emailEmpresa;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
