package org.example.crmiranchito.actions;

import org.example.crmiranchito.enums.EstadoReserva;
import org.example.crmiranchito.model.reserva.Reserva;
import org.openxava.actions.SaveAction;
import org.openxava.jpa.XPersistence;

public class MiSaveReservaAction extends SaveAction {

    @Override
    public void execute() throws Exception {

        Reserva reserva = (Reserva)getView().getEntity();

        if(reserva != null) {

            reserva.actualizarEstadoAutomatico();

            if(reserva.getEstado() == EstadoReserva.TERMINADA) {

            }

            XPersistence.getManager().merge(reserva);
            XPersistence.commit();

        }

        super.execute();
    }

}
