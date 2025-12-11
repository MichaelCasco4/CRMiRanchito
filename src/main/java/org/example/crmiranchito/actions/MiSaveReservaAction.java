package org.example.crmiranchito.actions;

import org.example.crmiranchito.enums.EstadoReserva;
import org.example.crmiranchito.model.reserva.Reserva;
import org.openxava.actions.SaveAction;
import org.openxava.jpa.XPersistence;

public class MiSaveReservaAction extends SaveAction {

    @Override
    public void execute() throws Exception {

        Long id = (Long) getView().getValue("id");

        Reserva reserva;

        if (id != null) {

            reserva = XPersistence.getManager().find(Reserva.class, id);
        }
        else {

            super.execute();
            return;
        }

        reserva.aplicarLogicaClienteLlego();

        XPersistence.getManager().merge(reserva);

        getView().setValue("estado", reserva.getEstado().toString());
        getView().setValue("clienteLlego", reserva.isClienteLlego());

        super.execute();
    }

}
