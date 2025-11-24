package org.example.crmiranchito.model.encuesta;

import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.model.Auditable;
import org.openxava.annotations.DefaultValueCalculator;
import org.openxava.annotations.ReadOnly;
import org.openxava.annotations.Stereotype;
import org.openxava.annotations.View;
import org.openxava.calculators.CurrentDateCalculator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "encuesta_satisfaccion")
@Getter
@Setter
@View(members =
        "Datos { fecha, usuarioId }\n" +
                "Reseña { comida; servicio; ambiente; tiempo; comentario }"
)
public class EncuestaSatisfaccion extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @DefaultValueCalculator(CurrentDateCalculator.class)
    @ReadOnly
    private Date fecha;

    @Column(name = "usuario_id")
    private Long usuarioId;

    private Integer comida;
    private Integer servicio;
    private Integer ambiente;
    private Integer tiempo;

    @Lob
    @Column(length = 4000)
    @Stereotype("MEMO")
    private String comentario;

    // Note: removed duplicate @PrePersist method because it's already defined in Auditable.
}

