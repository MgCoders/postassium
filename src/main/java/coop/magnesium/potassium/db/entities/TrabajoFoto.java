package coop.magnesium.potassium.db.entities;

import javax.persistence.*;

/**
 * Created by msteglich on 1/20/18.
 */
public class TrabajoFoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Trabajo trabajo;

    private String foto; //array bytes de la foto

    public TrabajoFoto() {
    }

    public TrabajoFoto(Trabajo trabajo, String foto) {
        this.trabajo = trabajo;
        this.foto = foto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trabajo getTrabajo() {
        return trabajo;
    }

    public void setTrabajo(Trabajo trabajo) {
        this.trabajo = trabajo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
