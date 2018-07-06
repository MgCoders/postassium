package coop.magnesium.potassium.db.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class NotificacionDestinatario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoNotificacion tipo;

    @NotNull
    @ManyToOne
    private Usuario usuario;

    public NotificacionDestinatario() {
    }

    public NotificacionDestinatario(TipoNotificacion tipo, Usuario usuario) {
        this.tipo = tipo;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoNotificacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacion tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
