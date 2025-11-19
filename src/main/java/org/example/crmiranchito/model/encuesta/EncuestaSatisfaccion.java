package org.example.crmiranchito.model.encuesta;


import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.model.Auditable;
import org.example.crmiranchito.model.cliente.Cliente;
import org.example.crmiranchito.model.reserva.Reserva;
import org.hibernate.validator.constraints.Range;
import org.openxava.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

@Entity
@Getter
@Setter

@Tab(properties =
" reserva.id, cliente.nombre, calificacionGeneral, fechaRespuesta, alertaInsatisfaccion")
@View(members =
"Datos { reserva } " +
"Cliente { cliente } " +
"Puntuacion { calificacionGeneral; comida; atencion; tiempoServicio; ambiente; recomendaciones } " +
"Comentario { comentario } " +
"Estado { fechaRespuesta; alertaInsatisfaccion } ")

public class EncuestaSatisfaccion extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Required
    @ReferenceView("Reserva")
    private Reserva reserva;

    @ManyToOne(fetch = FetchType.LAZY)
    @Required
    @ReadOnly
    private Cliente cliente;

    @Range(min = 1, max = 5)
    @Required
    private Integer calificacionGeneral;

    @Range(min = 1, max = 5)
    @Required
    private Integer comida;

    @Range(min = 1, max = 5)
    @Required
    private Integer atencion;

    @Range(min = 1, max = 5)
    @Required
    private Integer tiempoServicio;

    @Range(min = 1, max = 5)
    @Required
    private Integer ambiente;

    private Boolean recomendaciones;

    @Stereotype("MEMO")
    private  String comentario;

    @ReadOnly
    private Double promedio;

    @ReadOnly
    private LocalDate fechaRespuesta = LocalDate.now();

    @ReadOnly
    private Boolean alertaInsatisfaccion;



    @PostPersist
    @PostUpdate
    protected void actualizarEncuesta(){
        if(reserva != null){
            this.cliente = reserva.getCliente();
        }

        this.fechaRespuesta = LocalDate.now();
        int puntaje = 0;
        int total = 0;

        if(calificacionGeneral != null){ puntaje += calificacionGeneral; total++; }
        if(comida != null){ puntaje += comida; total++; }
        if(atencion != null){ puntaje += atencion; total++; }
        if(tiempoServicio != null){ puntaje += tiempoServicio; total++; }
        if(ambiente != null){ puntaje += ambiente; total++; }

        double promedio = total > 0 ? (double) puntaje / total : 5;
        this.alertaInsatisfaccion = promedio <= 2.5;
    }


    @AssertTrue(message = "El cliente debe de coincidir con el cliente de la reserva")
    private boolean isClienteCorrecto() {
        return reserva == null || cliente == null ||
                reserva.getCliente().getId().equals(cliente.getId());

    }
}

