package coop.magnesium.potassium.db.entities;

import javax.persistence.*;

/**
 * Created by msteglich on 1/20/18.
 */
public class TrabajoFoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFoto;

    @ManyToOne
    private Trabajo trabajo;

    private String foto; //array bytes de la foto

    public Long getIdFoto() {
        return idFoto;
    }

    public void setIdFoto(Long idFoto) {
        this.idFoto = idFoto;
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
