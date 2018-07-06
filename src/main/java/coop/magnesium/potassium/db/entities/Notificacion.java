package coop.magnesium.potassium.db.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by rsperoni on 19/01/18.
 */
@Entity
@JsonAutoDetect
@ApiModel
@Inheritance
@DiscriminatorColumn(name = "tipo_notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TipoNotificacion tipo;
    private String texto;
    @ManyToOne
    private Usuario usuario;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @ApiModelProperty(dataType = "date", example = "23-01-2017T16:45")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime fechaHora;
    private boolean enviado;

    public Notificacion() {
    }

    public Notificacion(TipoNotificacion tipo, Usuario usuario, String texto) {
        this.tipo = tipo;
        this.usuario = usuario;
        this.fechaHora = LocalDateTime.now();
        this.texto = texto;
        this.enviado =false;
    }

    public Notificacion(TipoNotificacion tipo, String texto) {
        this.tipo = tipo;
        this.fechaHora = LocalDateTime.now();
        this.texto = texto;
        this.enviado =false;
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

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public boolean isEnviado() {
        return enviado;
    }

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }

    @Override
    public String toString() {
        return "Notificacion{" +
                "id=" + id +
                ", tipo=" + tipo +
                ", texto='" + texto + '\'' +
                ", usuario=" + usuario +
                ", fechaHora=" + fechaHora +
                ", enviado=" + enviado +
                '}';
    }
}


