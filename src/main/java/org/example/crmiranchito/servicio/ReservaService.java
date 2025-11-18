package org.example.crmiranchito.servicio;

import lombok.Getter;
import lombok.Setter;
import org.directwebremoting.guice.ApplicationScoped;
import org.example.crmiranchito.model.reserva.Mesa;
import org.example.crmiranchito.model.reserva.Reserva;
import org.openxava.jpa.XPersistence;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ApplicationScoped
@Getter
@Setter
public class ReservaService {


    public boolean isMesaDisponible(Mesa mesa, LocalDate fecha, LocalTime hora) {

        String jpql = "SELECT r FROM Reserva r " +
                "Where r.mesa = :mesa " +
                "AND r.fechaReserva = :fecha " +
                "AND r.estado <> 'CANCELADA'";

        List<Reserva> reservas = XPersistence.getManager()
                .createQuery(jpql, Reserva.class)
                .setParameter("mesa", mesa)
                .setParameter("fecha", fecha)
                .getResultList();
        for (Reserva r : reservas) {

            if (r.getHoraReserva().equals(hora)) {
                return false;
            }
        }
        return true;

    }

}
