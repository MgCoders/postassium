package coop.magnesium.potassium.db.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;

/**
 * Created by pablo on 24/01/18.
 */
@Entity
@JsonAutoDetect
@ApiModel
@Table(name = "usuario_rubro", uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "rubro_id"}))
public class UsuarioRubro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "rubro_id", referencedColumnName = "id", nullable = false)
    private Rubro rubro;

    public UsuarioRubro() {
    }
}
