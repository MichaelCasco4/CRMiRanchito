package org.example.crmiranchito.actions;

import org.example.crmiranchito.model.reserva.Reserva;
import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;

public class ActualizarEstadoAlAbrir extends ViewBaseAction {

    @Override
    public void execute() throws Exception {

        Long id = (Long) getView().getValue("id");

        if(id == null)
            return;

        Reserva reserva = XPersistence.getManager().find(Reserva.class, id);
        if(reserva == null)
            return;

        reserva.actualizarEstadoAutomatico();
        XPersistence.getManager().merge(reserva);

        getView().refresh();

    }

}
