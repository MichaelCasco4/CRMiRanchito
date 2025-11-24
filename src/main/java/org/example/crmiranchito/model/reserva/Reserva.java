// java
package org.example.crmiranchito.model.reserva;

import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.enums.CanalComunicacion;
import org.example.crmiranchito.enums.EstadoReserva;
import org.example.crmiranchito.listeners.ReservaListener;
import org.example.crmiranchito.model.Auditable;
import org.example.crmiranchito.model.usuario.Usuario;
import org.example.crmiranchito.model.cliente.Cliente;
import org.openxava.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@EntityListeners(ReservaListener.class)
@View(name = "Reserva", members =
        "Cliente { cliente } " +
                "Reserva { fechaReserva; horaReserva; cantidadPersonas; canal } " +
                "Mesa { mesa } " +
                "Control { estado; usuario; createdOn }")
@Tab(properties = "cliente.nombre, fechaReserva, horaReserva, cantidadPersonas, mesa.numero, estado")
public class Reserva extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Required
    private Cliente cliente;

    @Required
    private LocalDate fechaReserva;

    @Required
    private LocalTime horaReserva;

    @Required
    @Min(1)
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

    @AssertTrue(message = "La fecha y hora de la reserva no puede ser en el pasado")
    private boolean isFechaHoraValida() {
        if (fechaReserva == null || horaReserva == null)
            return true;
        return LocalDateTime.of(fechaReserva, horaReserva)
                .isAfter(LocalDateTime.now());
    }

    @AssertTrue(message = "Cantidad excede capacidad de la mesa")
    private boolean isCantidadPersonasValida() {
        return mesa == null || mesa.getCapacidad() == null ||
                cantidadPersonas == null ||
                cantidadPersonas <= mesa.getCapacidad();
    }
}






