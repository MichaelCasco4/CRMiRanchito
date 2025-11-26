package org.example.crmiranchito.scheduler;

import org.directwebremoting.guice.ApplicationScoped;
import org.example.crmiranchito.enums.EstadoMesa;
import org.example.crmiranchito.enums.EstadoReserva;
import org.example.crmiranchito.model.reserva.Mesa;
import org.example.crmiranchito.model.reserva.Reserva;
import org.openxava.jpa.XPersistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@ApplicationScoped
public class EstadoMesaSch {

    private static final int DURACION_RESERVA = 90;
    private static final int ANTICIPACION_RESERVA = 60;


    public EstadoMesaSch() {

        new Timer(true).scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                try{
                    XPersistence.getManager().getTransaction().begin();
                    actualizarEstados();
                    XPersistence.getManager().getTransaction().commit();
                }
                catch (Exception e){
                    if(XPersistence.getManager().getTransaction().isActive()){
                        XPersistence.getManager().getTransaction().rollback();
                    }

                    System.err.println("Error Actualizando estado de mesas: " + e.getMessage());
                }
            }
        }, 0,60_000);
    }

    private void actualizarEstados() {

        List<Mesa> mesas = XPersistence.getManager()
                .createQuery("SELECT m FROM Mesa m", Mesa.class)
                .getResultList();

        LocalDateTime ahora = LocalDateTime.now();

        for(Mesa mesa : mesas) {

            List<Reserva> reservas = XPersistence.getManager()
                    .createQuery("SELECT r FROM Reserva r " +
                            "WHERE r. mesa = :mesa " +
                            "AND r.estado <> :cancelada " +
                            "ORDER BY r.fechaReserva ASC, r.horaReserva ASC",
                            Reserva.class)
                    .setParameter("mesa", mesa)
                    .setParameter("cancelada", EstadoReserva.CANCELADA)
                    .getResultList();

            if(reservas.isEmpty()) {

                cambiar(mesa, EstadoMesa.DISPONIBLE);
                continue;

            }

            Reserva r = reservas.get(0);
            LocalDateTime ini = LocalDateTime.of(r.getFechaReserva(), r.getHoraReserva());
            LocalDateTime fin = ini.plusMinutes(DURACION_RESERVA);
            LocalDateTime alerta = ini.minusMinutes(ANTICIPACION_RESERVA);

            if(ahora.isAfter(fin)) cambiar(mesa, EstadoMesa.DISPONIBLE);
            else if (ahora.isAfter(ini)) cambiar(mesa, EstadoMesa.OCUPADA);
            else if (ahora.isAfter(alerta)) cambiar(mesa, EstadoMesa.RESERVADA);
            else cambiar(mesa, EstadoMesa.DISPONIBLE);

        }
    }

    private void cambiar(Mesa mesa, EstadoMesa estado) {
        if(mesa.getEstado() != estado) {
            mesa.setEstado(estado);
            XPersistence.getManager().merge(mesa);
        }
    }

}


