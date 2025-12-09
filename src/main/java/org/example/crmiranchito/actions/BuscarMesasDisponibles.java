package org.example.crmiranchito.actions;

import org.example.crmiranchito.model.reserva.Mesa;
import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;

import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class BuscarMesasDisponibles extends ViewBaseAction {

    @Override
    public void execute() {
        LocalDate fecha = (LocalDate) getView().getValue("fechaReserva");
        LocalTime hora = (LocalTime) getView().getValue("horaReserva");
        Integer cantidad = (Integer) getView().getValue("cantidadPersonas");

        if(fecha == null || hora == null || cantidad == null ){
            addError("Debe ingresar la fecha de reserva del persona");
            return;
        }

        Query q = XPersistence.getManager().createQuery(
                "SELECT m FROM Mesa m WHERE m.capacidad >= :cap ORDER BY m.capacidad "
        );

        q.setParameter("cap", cantidad);

        List<Mesa> mesas = q.getResultList();

        if(mesas.isEmpty()){
            addError("No hay mesas disponibles para esos criterios");
        } else {

            Mesa mesa = mesas.get(0);

            getView().setValue("mesa.id", mesa.getId());
            addMessage("Mesa sugerida: " + mesa.getNumero() + " (Capacidad: " + mesa.getCapacidad() + ")");

        }
    }
}
