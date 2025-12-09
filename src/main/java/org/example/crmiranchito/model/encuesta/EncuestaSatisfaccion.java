package org.example.crmiranchito.model.encuesta;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.Range;
import org.openxava.annotations.*;

import javax.persistence.*;
import java.time.LocalDate;

@Embeddable
@Getter
@Setter


public class EncuestaSatisfaccion {

    @Stereotype("STARS")
    @Range(min = 1, max = 5)
    @Required
    private Integer calificacionGeneral;

    @Stereotype("STARS")
    @Range(min = 1, max = 5)
    @Required
    private Integer comida;

    @Stereotype("STARS")
    @Range(min = 1, max = 5)
    @Required
    private Integer atencion;

    @Stereotype("STARS")
    @Range(min = 1, max = 5)
    @Required
    private Integer tiempoServicio;

    @Stereotype("STARS")
    @Range(min = 1, max = 5)
    @Required
    private Integer ambiente;

    private Boolean recomendaciones;

    @Stereotype("MEMO")
    private String comentario;

    @ReadOnly
    private LocalDate fechaRespuesta = LocalDate.now();

    @ReadOnly
    @Formula("(CASE WHEN((calificacionGeneral + comida + atencion + tiempoServicio + ambiente) / 5.0) <= 2 THEN true ELSE false END)")
    private Boolean alertaInsatisfaccion;

    @ReadOnly
    @Stereotype("RATING")
    @Formula("((calificacionGeneral + comida + atencion + tiempoServicio + ambiente) / 5.0)")
    private Double promedio;



}
