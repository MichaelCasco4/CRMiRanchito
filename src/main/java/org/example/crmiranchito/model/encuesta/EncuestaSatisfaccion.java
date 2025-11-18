package org.example.crmiranchito.model.encuesta;


import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.model.Auditable;
import org.example.crmiranchito.model.cliente.Cliente;
import org.example.crmiranchito.model.reserva.Reserva;
import org.hibernate.validator.constraints.Range;
import org.openxava.annotations.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@View(members =
"Datos { reserva; cliente } " +
"Puntuacion { calificacionGeneral; comida; atencion; tiempoServicio; ambiente; recomendaciones } " +
"Comentario { comentario } " +
"Estado { fechaRespuesta; alertaInsatisfaccion } " )

@Tabs(@Tab(properties = "reserva.id, cliente.nombre, calificacionGeneral, fechaRespuesta, alertaInsatisfaccion "))

public class EncuestaSatisfaccion extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Required
    private Reserva reserva;

    @ManyToOne(fetch = FetchType.LAZY)
    @Required
    private Cliente cliente;

    @Range(min = 1, max = 5)
    @Required
    private Integer calificacionGeneral;

    @Range(min = 1, max = 5)
    @Required
    private Integer comida;

    @Range(min = 1, max = 5)
    @Required
    private Integer tiempoServicio;

    @Range(min = 1, max = 5)
    @Required
    private Integer ambiente;

    private Boolean recomendaciones;

    @Stereotype("MEMO")
    private  String comentario;

    private LocalDate fechaRespuesta = LocalDate.now();

    @ReadOnly
    private Boolean alertaInsatisfaccion;

    @PreUpdate
    @PrePersist
    private void calcularAlerta(){

        alertaInsatisfaccion = calificacionGeneral != null && calificacionGeneral <= 2;
    }

    @PrePersist
    @PreUpdate
    private void validarClienteReserva(){

        if(reserva != null && cliente != null && reserva.getCliente() != null){
            if(!reserva.getCliente().getId().equals(cliente.getId())){
                throw new javax.validation.ValidationException("El cliente debe de coincidir con la reserva ");
            }
        }
    }
}
