package coop.magnesium.potassium.db.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by rsperoni on 16/11/17.
 */
@Entity
@JsonAutoDetect
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El usuario debe tener un email")
    @Column(unique = true)
    private String email;

    @NotNull(message = "El usuario debe tener un nombre")
    private String nombre;

    private String apellido;

    private String password;

    @NotNull
    private String role = Role.USER.name();

    @Transient
    private String token;

    @NotNull
    private boolean login;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<UsuarioRubro> usuarioRubros = new HashSet<>();

    private String pin;

    public Usuario() {
    }

    public Usuario(String email, String nombre, String password, String role, boolean login) {
        this.email = email;
        this.nombre = nombre;
        this.password = password;
        this.role = role;
        this.login = login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<UsuarioRubro> getUsuarioRubros() {
        return usuarioRubros;
    }

    public void setUsuarioRubros(Set<UsuarioRubro> usuarioRubros) {
        this.usuarioRubros = usuarioRubros;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
