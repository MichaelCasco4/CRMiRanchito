package org.example.crmiranchito.listeners;

import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.enums.EstadoMesa;
import org.example.crmiranchito.enums.EstadoReserva;
import org.example.crmiranchito.model.reserva.Mesa;
import org.example.crmiranchito.model.reserva.Reserva;
import org.openxava.jpa.XPersistence;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReservaListener {

    private static final int DURACION_RESERVA = 90;
    private static final int DURACION_RESERVAMAX = 60;

    @PrePersist
    public void alCrear(Reserva reserva) {
        validarDisponibilidad(reserva);
        actualizarEstadoMesa(reserva);
    }

    @PreUpdate
    public void alActualizar(Reserva reserva) {
        validarDisponibilidad(reserva);
        actualizarEstadoMesa(reserva);
    }

    private void actualizarEstadoMesa(Reserva reserva) {

        Mesa mesa = reserva.getMesa();
        if(mesa != null)
            return;

        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicio = LocalDateTime.of(reserva.getFechaReserva(), reserva.getHoraReserva());

        if(reserva.getEstado() == EstadoReserva.CANCELADA) {
            mesa.setEstado(EstadoMesa.DISPONIBLE);

        } else if (ahora.isAfter(inicio)) {
            mesa.setEstado(EstadoMesa.OCUPADA);

        } else if (ahora.isAfter(inicio.minusMinutes(DURACION_RESERVAMAX))) {
            mesa.setEstado(EstadoMesa.RESERVADA);

        }
        else  {
            mesa.setEstado(EstadoMesa.DISPONIBLE);
        }

        XPersistence.getManager().merge(mesa);
    }


    public void validarDisponibilidad(Reserva reserva) {

        if(reserva.getMesa() == null)
            return;

        LocalDateTime nuevaInicio = LocalDateTime.of(reserva.getFechaReserva(), reserva.getHoraReserva());
        LocalDateTime nuevaFin = nuevaInicio.plusMinutes(DURACION_RESERVA);

        String jpql = "SELECT r FROM Reserva r " +
                "WHERE r.mesa = :mesa " +
                "AND r.fechaReserva = :fecha " +
                "AND r.id <> :id " +
                "AND  r.estado <> 'CANCELADA'";

        List<Reserva> reservasExistentes = XPersistence.getManager()
                .createQuery(jpql, Reserva.class)
                .setParameter("mesa", reserva.getMesa())
                .setParameter("fecha", reserva.getFechaReserva())
                .setParameter("id", reserva.getId() == null ? -1L : reserva.getId())
                .getResultList();

        for(Reserva existente : reservasExistentes){

            LocalDateTime existenteInicio = LocalDateTime.of(existente.getFechaReserva(), existente.getHoraReserva());
            LocalDateTime existenteFin = existenteInicio.plusMinutes(DURACION_RESERVA);

            if(nuevaInicio.isBefore(existenteFin) && nuevaFin.isAfter(existenteInicio)){

                throw new javax.validation.ValidationException(
                        "La mesa ya esta ocupada entre " +
                                existenteInicio.toLocalTime() + " y " +
                                existenteFin.toLocalTime()
                );
            }
        }
    }

}
