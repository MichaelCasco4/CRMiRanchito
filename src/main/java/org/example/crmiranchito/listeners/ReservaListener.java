package org.example.crmiranchito.listeners;

import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.model.reserva.Reserva;
import org.example.crmiranchito.servicio.ReservaService;
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
        List<Reserva> reservas = XPersistence.getManager()
                .createQuery("SELECT r FROM Reserva r " +
                        "WHERE r.mesa = :mesa " +
                        "AND r.fechaReserva = :fecha " +
                        "AND r.estado <> 'CANCELADA'", Reserva.class)
                .setParameter("mesa", reserva.getMesa())
                .setParameter("fecha", reserva.getFechaReserva())
                .getResultList();

        reservas.forEach(r -> {

            if(r.getHoraReserva().equals(reserva.getHoraReserva())) {
                throw new javax.validation.ValidationException(
                        "La mesa ya esta reservada a esa hora "
                );
            }
        });
    }
}
