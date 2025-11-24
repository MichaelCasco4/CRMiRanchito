package org.example.crmiranchito.actions;

import org.example.crmiranchito.model.reserva.Mesa;
import org.example.crmiranchito.enums.EstadoMesa;
import org.example.crmiranchito.model.reserva.Reserva;
import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;

import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class AsignarMesa extends ViewBaseAction {

    @Override
    public void execute() throws Exception {

        LocalDate fecha = (LocalDate) getView().getValue("fechaReserva");
        LocalTime hora = (LocalTime) getView().getValue("horaReserva");
        Integer cantidad = (Integer) getView().getValue("cantidadPersonas");

        if (fecha == null || hora == null || cantidad == null) {
            addError("Debe ingresar fecha, hora y cantidad de personas");
            return;
        }

        String jpql = "SELECT m FROM Mesa m WHERE m.capacidad >= :cant " +
                "AND m.estado <> 'OCUPADA' " +
                "ORDER BY m.capacidad ASC";

        Query query = XPersistence.getManager()
                .createQuery(jpql, Mesa.class)
                .setParameter("cant", cantidad);

        List<Mesa> mesas = query.getResultList();

        if (mesas.isEmpty()) {
            addError("No hay mesas disponibles");
        } else {

            Mesa mesaSeleccionada = mesas.get(0);
            getView().setValue("mesa", mesaSeleccionada);
            addMessage("Mesa seleccionada automaticamente: " + mesaSeleccionada.getNumero());
        }
    }

}












