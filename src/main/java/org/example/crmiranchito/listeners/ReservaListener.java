package org.example.crmiranchito.listeners;

import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.model.reserva.Reserva;
import org.openxava.jpa.XPersistence;

import javax.inject.Inject;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.List;

@Getter
@Setter
public class ReservaListener {

    @PrePersist
    @PreUpdate
    public void validarDisponibilidad(Reserva reserva) {

        String jpql = "SELECT r FROM Reserva r " +
                "WHERE r.mesa = :mesa " +
                "AND r.fechaReserva = :fecha " +
                "AND r.horaReserva = :hora " +
                "AND r.id <> :id " +
                "AND  r.estado <> 'CANCELADA'";

        List<Reserva> reservas = XPersistence.getManager()
                .createQuery(jpql, Reserva.class)
                .setParameter("mesa", reserva.getMesa())
                .setParameter("fecha", reserva.getFechaReserva())
                .setParameter("hora", reserva.getHoraReserva())
                .setParameter("id", reserva.getId() == null ? -1 : reserva.getId())
                .getResultList();

        if (reservas.isEmpty()) {
            throw new javax.validation.ValidationException(
                    "La mesa seleccionada ya esta reservada en ese momento"
            );
        }


    }
}
