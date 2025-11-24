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
    public void execute() throws Exception {
        LocalDate fecha = (LocalDate) getView().getValue("fechaReserva");
        LocalTime hora = (LocalTime) getView().getValue("horaReserva");
        Integer cantidad = (Integer) getView().getValue("cantidadPersonas");

        if(fecha == null || hora == null || cantidad == null ){
            addError("Debe ingresar la fecha de reserva del persona");
            return;
        }

        Query query = XPersistence.getManager().createQuery(
                "SELECT m FROM Mesa m WHERE m.capacidad >= :cap"
        );

        query.setParameter("cap", cantidad);

        List<Mesa> disponibles = query.getResultList();

        if(disponibles.isEmpty()){
            addError("No hay mesas disponibles para esos criterios");
            return;
        }

        getView().setValue("mesa",  disponibles.get(0));
        addMessage("Mesa seleccionada automaticamente por disponibilidad");
        getView().refresh();

    }
}
