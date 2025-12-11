package org.example.crmiranchito.job;

import org.example.crmiranchito.model.reserva.Reserva;
import org.example.crmiranchito.enums.EstadoReserva;
import org.openxava.jpa.XPersistence;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
public class ReservaExpirationJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {

        EntityManager em = XPersistence.createManager();  // << ESTE ES EL CORRECTO
        em.getTransaction().begin();

        try {

            List<Reserva> reservas = em.createQuery(
                            "SELECT r FROM Reserva r WHERE r.estado <> :cancelada", Reserva.class)
                    .setParameter("cancelada", EstadoReserva.CANCELADA)
                    .getResultList();

            LocalDateTime ahora = LocalDateTime.now();

            for (Reserva reserva : reservas) {

                LocalDateTime inicio = LocalDateTime.of(reserva.getFechaReserva(), reserva.getHoraReserva());
                LocalDateTime fin = inicio.plusMinutes(reserva.getDuracionMinutos());
                LocalDateTime limite = inicio.plusMinutes(20);

                if (!reserva.isClienteLlego() &&
                        reserva.getEstado() == EstadoReserva.PENDIENTE &&
                        ahora.isAfter(limite)) {

                    reserva.setEstado(EstadoReserva.EXPIRADA);
                }
                else if (ahora.isAfter(fin) &&
                        reserva.getEstado() != EstadoReserva.TERMINADA &&
                        reserva.getEstado() != EstadoReserva.EXPIRADA) {

                    reserva.setEstado(EstadoReserva.TERMINADA);
                }

                em.merge(reserva);
            }

            em.getTransaction().commit();
        }
        catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
        finally {
            em.close();
        }
    }

}
