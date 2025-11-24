package org.example.crmiranchito.scheduler;

import org.directwebremoting.guice.ApplicationScoped;
import org.example.crmiranchito.enums.EstadoMesa;
import org.example.crmiranchito.enums.EstadoReserva;
import org.example.crmiranchito.model.reserva.Mesa;
import org.example.crmiranchito.model.reserva.Reserva;
import org.openxava.jpa.XPersistence;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@ApplicationScoped
public class EstadoMesaSch {

    private static final int DURACION_RESERVA = 90;
    private static final int DURACION_RESERVAMAX = 60;


    public EstadoMesaSch() {

        Timer timer = new Timer(true);

        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            @Transactional
            public void run() {
                actualizarEstados();
            }
        }, 0, 60 * 1000);

    }

    @Transactional
    private void actualizarEstados() {

        List<Mesa> mesas = XPersistence.getManager()
                .createQuery("SELECT m FROM Mesa m", Mesa.class)
                .getResultList();

        LocalDateTime ahora = LocalDateTime.now();

        for (Mesa mesa : mesas) {


            List<Reserva> reservas = XPersistence.getManager()
                    .createQuery("SELECT r FROM Reserva r " +
                                    "WHERE r.mesa = :mesa" +
                                    " AND r.estado <> 'CANCELADA'", Reserva.class)
                    .setParameter("mesa", mesa)
                    .getResultList();

            if(reservas.isEmpty()) {
                mesa.setEstado(EstadoMesa.DISPONIBLE);
                XPersistence.getManager().merge(mesa);
                continue;
            }

            Reserva res = reservas.get(0);
            LocalDateTime inicio = LocalDateTime.of(res.getFechaReserva(), res.getHoraReserva());
            LocalDateTime reservaDesde = inicio.minusMinutes(DURACION_RESERVAMAX);
            LocalDateTime fin = inicio.plusMinutes(DURACION_RESERVA);

            if(ahora.isAfter(fin)) {

                mesa.setEstado(EstadoMesa.DISPONIBLE);
            } else if (ahora.isAfter(inicio)) {

                mesa.setEstado(EstadoMesa.OCUPADA);

            } else if (ahora.isAfter(reservaDesde)) {

                mesa.setEstado(EstadoMesa.RESERVADA);

            } else {

                mesa.setEstado(EstadoMesa.DISPONIBLE);

            }

            XPersistence.getManager().merge(mesa);

        }
    }
}


