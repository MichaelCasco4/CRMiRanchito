package org.example.crmiranchito.model.encuesta;


import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.model.Auditable;
import org.example.crmiranchito.model.cliente.Cliente;
import org.example.crmiranchito.model.reserva.Reserva;
import org.hibernate.validator.constraints.Range;
import org.openxava.annotations.*;
import org.openxava.jpa.XPersistence;

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
"Promedio { promedio } " +
"Comentario { comentario } " +
"Estado { fechaRespuesta; alertaInsatisfaccion } ")

public class EncuestaSatisfaccion extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Required
    private Reserva reserva;

    @ManyToOne(fetch = FetchType.LAZY)
    @Required
    @ReadOnly
    private Cliente cliente;

    @Stereotype("RATING")
    @Range(min = 1, max = 5)
    @Required
    private Integer calificacionGeneral;

    @Stereotype("RATING")
    @Range(min = 1, max = 5)
    @Required
    private Integer comida;

    @Stereotype("RATING")
    @Range(min = 1, max = 5)
    @Required
    private Integer atencion;

    @Stereotype("RATING")
    @Range(min = 1, max = 5)
    @Required
    private Integer tiempoServicio;

    @Stereotype("RATING")
    @Range(min = 1, max = 5)
    @Required
    private Integer ambiente;

    private Boolean recomendaciones;

    @Stereotype("MEMO")
    private  String comentario;

    @ReadOnly
    @Stereotype("RATING")
    private Integer promedio;

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

        int puntaje = calificacionGeneral + comida + atencion + tiempoServicio + ambiente;
        int total = 5;
        double promdioCalc = (double)  puntaje / total;

        this.promedio = (int) Math.round(promdioCalc);
        this.alertaInsatisfaccion = this.promedio <= 2;

    }


    @AssertTrue(message = "El cliente debe de coincidir con el cliente de la reserva")
    private boolean isClienteCorrecto() {
        return reserva == null || cliente == null ||
                reserva.getCliente().getId().equals(cliente.getId());

    }

    @AssertTrue(message = "Esta reserva ya tiene una encuesta registrada")
    private boolean isPrimeraEncuesta(){

        if(reserva == null)
            return true;

        Long count = XPersistence.getManager()
                .createQuery("SELECT COUNT(e) FROM EncuestaSatisfaccion e WHERE e.reserva = :reserva AND e.id <> :id", Long.class)
                .setParameter("reserva", reserva)
                .setParameter("id", id == null ? - 1L : id)
                .getSingleResult();

        return count == 0;
    }
}

