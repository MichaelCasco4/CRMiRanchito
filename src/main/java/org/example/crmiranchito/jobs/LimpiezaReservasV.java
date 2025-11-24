package org.example.crmiranchito.jobs;


import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.enums.EstadoMesa;
import org.example.crmiranchito.enums.EstadoReserva;
import org.example.crmiranchito.model.reserva.Mesa;
import org.example.crmiranchito.model.reserva.Reserva;
import org.openxava.jpa.XPersistence;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter

public class LimpiezaReservasV {

    private static final int DURACION = 90;

    @Transactional
    public void ejecutar(){

        LocalDateTime ahora = LocalDateTime.now();

        String jpql = "SELECT r FROM Reserva r " +
                "WHERE r.estado NOT IN ('CANCELADA', 'TERMINADA')";

        List<Reserva> reservas = XPersistence.getManager().createQuery(jpql, Reserva.class)
                .getResultList();

        for(Reserva r : reservas){

            LocalDateTime inicio = LocalDateTime.of(r.getFechaReserva(), r.getHoraReserva());
            LocalDateTime fin = inicio.plusMinutes(DURACION);

            if(ahora.isAfter(fin)) {

                r.setEstado(EstadoReserva.EXPIRADA);

                Mesa m = r.getMesa();
                if(m!=null) {
                    m.setEstado(EstadoMesa.DISPONIBLE);
                    XPersistence.getManager().merge(m);
                }
                XPersistence.getManager().merge(r);
            }
        }
    }
}
