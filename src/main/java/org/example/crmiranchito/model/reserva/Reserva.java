package org.example.crmiranchito.model.reserva;

import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.enums.CanalComunicacion;
import org.example.crmiranchito.enums.EstadoReserva;

import org.example.crmiranchito.model.Auditable;
import org.example.crmiranchito.model.encuesta.EncuestaSatisfaccion;
import org.example.crmiranchito.model.usuario.Usuario;
import org.example.crmiranchito.model.cliente.Cliente;
import org.hibernate.validator.constraints.Range;
import org.openxava.annotations.*;
import org.openxava.jpa.XPersistence;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter

@View(members =
"Cliente { cliente } " +
"Reserva { fechaReserva; horaReserva; duracionMinutos; cantidadPersonas; canal } " +
"Mesa Asignada { mesa } " +
"Control { estado; usuario } " +
"Encuestas { encuestas } ")

@Tab(properties = "cliente.nombre, fechaReserva, horaReserva, cantidadPersonas, mesa.numero, estado")

public class Reserva extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Required
    @DescriptionsList(descriptionProperties = " nombre ")
    private Cliente cliente;

    @Required
    private LocalDate fechaReserva;

    @Required
    private LocalTime horaReserva;

    @Required
    @Min(1)
    private Integer cantidadPersonas;

    @Required
    @Range(min = 30, max = 240)
    private Integer duracionMinutos;

    @ManyToOne(fetch = FetchType.LAZY)
    @Required
    @DescriptionsList(descriptionProperties = "numero, capacidad")
    private Mesa mesa;

    @Enumerated(EnumType.STRING)
    @Required
    private CanalComunicacion canal;

    @Enumerated(EnumType.STRING)
    private EstadoReserva estado = EstadoReserva.PENDIENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @DescriptionsList(descriptionProperties = "username")
    private Usuario usuario;

    /*<---------------------------------------->*/

    @ElementCollection
    @ListProperties("calificacionGeneral, comida, atencion, tiempoServicio, ambiente, recomendaciones, comentario")
    private Collection<EncuestaSatisfaccion> encuestas;


    /*<---------------------------------------->*/

    @AssertTrue(message = "La fecha y hora de la reserva no puede ser en el pasado")
    private boolean isFechaHoraValida() {

        if(estado == EstadoReserva.TERMINADA)
            return true;

        if(fechaReserva == null || horaReserva == null)
            return true;

        return LocalDateTime.of(fechaReserva, horaReserva).isAfter(LocalDateTime.now());

    }

    @AssertTrue(message = "Cantidad excede capacidad de la mesa")
    private boolean isCantidadPersonasValida() {

        if(mesa == null || cantidadPersonas == null)
            return true;
        return cantidadPersonas <= mesa.getCapacidad();
    }

    private boolean isHorarioDisponible(){

        if(mesa == null || fechaReserva == null || horaReserva == null)
            return true;

        LocalDateTime nuevoInicio = LocalDateTime.of(fechaReserva, horaReserva);
        LocalDateTime nuevaFin = nuevoInicio.plusMinutes(duracionMinutos);

        List<EstadoReserva> excluir = List.of(EstadoReserva.CANCELADA, EstadoReserva.TERMINADA);

        List<Reserva> existentes = XPersistence.getManager().createQuery(

                "SELECT r FROM Reserva r WHERE r.mesa = :mesa AND r.fechaReserva = :fecha AND r.id <> :id AND r.estado NOT IN :excluir", Reserva.class)
                .setParameter("mesa", mesa)
                .setParameter("fecha", fechaReserva)
                .setParameter("id", id == null ? -1L : id)
                .setParameter("excluir", excluir)
                .getResultList();

        for (Reserva r : existentes){

            LocalDateTime inicio = LocalDateTime.of(r.getFechaReserva(), r.getHoraReserva());
            LocalDateTime fin = inicio.plusMinutes(r.getDuracionMinutos());

            boolean solapa = nuevoInicio.isBefore(fin) && nuevaFin.isAfter(inicio);
            if (solapa)
                return false;
        }

        return true;
    }

    public void actualizarEstadoAutomatico(){

        LocalDateTime inicio = LocalDateTime.of(fechaReserva, horaReserva);
        LocalDateTime fin = inicio.plusMinutes(duracionMinutos);
        LocalDateTime ahora = LocalDateTime.now();

        if(estado == EstadoReserva.CANCELADA)
            return;

        if(ahora.isBefore(inicio)) {

            estado = EstadoReserva.PENDIENTE;

        } else if (ahora.isAfter(inicio) && ahora.isBefore(fin)) {

            estado = EstadoReserva.CONFIRMADA;

        } else if (ahora.isAfter(fin)) {

            estado = EstadoReserva.TERMINADA;

            }

        }

    }








