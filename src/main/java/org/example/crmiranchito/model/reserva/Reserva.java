package org.example.crmiranchito.model.reserva;

import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.enums.CanalComunicacion;
import org.example.crmiranchito.enums.EstadoReserva;
import org.example.crmiranchito.listeners.ReservaListener;
import org.example.crmiranchito.model.Auditable;
import org.example.crmiranchito.model.usuario.Usuario;
import org.example.crmiranchito.model.cliente.Cliente;
import org.example.crmiranchito.servicio.ReservaService;
import org.openxava.annotations.*;

import javax.inject.Inject;
import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@EntityListeners(ReservaListener.class)
@Getter
@Setter
@View(name = "Reserva", members =
"Cliente { cliente } " +
"Reserva { fechaReserva; horaReserva; cantidadPersonas; canal } " +
"Mesa { mesa } " +
"Control { estado; usuario; fechaCreacion }")

@Tabs(@Tab(properties = "cliente.nombre, fechaReserva, horaReserva, cantidadPersonas, mesa.numero, estado "))

public class Reserva extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Required
    private Cliente cliente;

    @Required
    private LocalDate fechaReserva;

    @Required
    private LocalTime horaReserva;

    @Required
    private Integer cantidadPersonas;

    @ManyToOne(fetch = FetchType.LAZY)
    @Required
    private Mesa mesa;

    @Enumerated(EnumType.STRING)
    @Required
    private CanalComunicacion canal;

    @Enumerated(EnumType.STRING)
    private EstadoReserva estado = EstadoReserva.PENDIENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    @ReadOnly
    private LocalDate fechaCreacion;


    @AssertTrue(message = "La fecha y hora de la reserva no puede ser en el pasado")
    private boolean isFechaHoraValida() {
        if (fechaReserva == null || horaReserva == null)
            return true;
        return LocalDateTime.of(fechaReserva, horaReserva)
                .isAfter(LocalDateTime.now());
    }

    @AssertTrue(message = "Cantidad de personas debe de ser > 0 y no exceda la capacidad")
    private boolean isCantidadPersonasValida() {
        if (cantidadPersonas == null || cantidadPersonas <= 0)
            return false;
        if (mesa != null && mesa.getCapacidad() != null)
            return cantidadPersonas <= mesa.getCapacidad();
        return true;
    }
}





