package org.example.crmiranchito.listeners;

import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.enums.EstadoReserva;
import org.example.crmiranchito.model.reserva.Reserva;
import org.openxava.jpa.XPersistence;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReservaListener {

    private static final int DURACION_RESERVA = 90;


    @PrePersist
    @PreUpdate
    public void validarDisponibilidad(Reserva reserva) {

        if(reserva.getMesa() == null)
            return;


        LocalDateTime nuevaInicio = LocalDateTime.of(reserva.getFechaReserva(), reserva.getHoraReserva());
        LocalDateTime nuevaFin = nuevaInicio.plusMinutes(DURACION_RESERVA);

        List<Reserva> existentes = XPersistence.getManager()
                .createQuery("SELECT r FROM Reserva r " +
                        "WHERE r.mesa = :mesa " +
                        "AND r.fechaReserva = :fecha " +
                        "AND r.id <> :id " +
                        "AND r.estado <> :cancelada",
                        Reserva.class)
                .setParameter("mesa", reserva.getMesa())
                .setParameter("fecha", reserva.getFechaReserva())
                .setParameter("id", reserva.getId() == null ? -1L : reserva.getId())
                .setParameter("cancelada", EstadoReserva.CANCELADA)
                .getResultList();

        for (Reserva r : existentes) {

            LocalDateTime inicio = LocalDateTime.of(r.getFechaReserva(), r.getHoraReserva());
            LocalDateTime fin = inicio.plusMinutes(DURACION_RESERVA);

            if(nuevaInicio.isBefore(fin) && nuevaFin.isAfter(inicio)) {

                throw new javax.validation.ValidationException(
                        "Mesa Ocupada entre " + inicio.toLocalTime() + " y " + fin.toLocalTime()
                );
            }
        }


    }

}
