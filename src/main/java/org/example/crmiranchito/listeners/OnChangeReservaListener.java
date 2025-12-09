package org.example.crmiranchito.listeners;

import org.example.crmiranchito.model.reserva.Reserva;
import org.openxava.actions.OnChangePropertyBaseAction;
import org.openxava.jpa.XPersistence;

public class OnChangeReservaListener extends OnChangePropertyBaseAction {

    @Override
    public void execute() throws Exception {

        if(getNewValue() == null){

            getView().setValue("cliente", null);
            return;

        }

        Long reservaId = Long.valueOf(getNewValue().toString());
        Reserva reserva = XPersistence.getManager().find(Reserva.class, reservaId);

        if(reserva != null){

            getView().setValue("cliente", reserva.getCliente());

        }

    }

}
