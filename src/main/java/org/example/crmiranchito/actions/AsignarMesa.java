package org.example.crmiranchito.actions;

import org.example.crmiranchito.model.reserva.Mesa;
import org.example.crmiranchito.model.reserva.Reserva;
import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;

import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

//Probar usar SaveAction

public class AsignarMesa extends ViewBaseAction {

    @Override
    public void execute() {

        LocalDate fecha = (LocalDate) getView().getValue("fechaReserva");
        LocalTime hora = (LocalTime) getView().getValue("horaReserva");
        Integer cantidad = (Integer) getView().getValue("cantidadPersonas");

        if (fecha == null || hora == null || cantidad == null) {
            addError("Debe ingresar fecha, hora y cantidad de personas");
            return;
        }

        Query queryMesas = XPersistence.getManager().createQuery(
                "SELECT m FROM Mesa m WHERE m.capacidad >= :cap ORDER BY m.capacidad ASC"
        );
        queryMesas.setParameter("cap", cantidad);

        List<Mesa> mesas = queryMesas.getResultList();

        for (Mesa mesa : mesas) {

            Query q = XPersistence.getManager().createQuery(
                    "SELECT r FROM Reserva r WHERE r.mesa= :mesa AND r.fechaReserva = :fecha"
            );
            q.setParameter("mesa", mesa);
            q.setParameter("fecha", fecha);

            List<Reserva> reservas = q.getResultList();

            if (reservas.isEmpty()) {

                getView().setValue("mesa.id", mesa.getId());
                addMessage("Mesa asignada automaticamente: " + mesa.getNumero());
                return;

            }
        }

        addError("No hay mesas disponibles para esa fecha y hora");
    }
}












