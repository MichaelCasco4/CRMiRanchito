package org.example.crmiranchito.actions;

import org.example.crmiranchito.enums.EstadoReserva;
import org.example.crmiranchito.model.reserva.Mesa;
import org.example.crmiranchito.enums.EstadoMesa;
import org.example.crmiranchito.model.reserva.Reserva;
import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;

import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AsignarMesa extends ViewBaseAction {

    @Override
    @SuppressWarnings("unchecked")
    public void execute() {

        LocalDate fecha = (LocalDate) getView().getValue("fechaReserva");
        LocalTime hora = (LocalTime) getView().getValue("horaReserva");
        Integer cantidad = (Integer) getView().getValue("cantidadPersonas");

        if (fecha == null || hora == null || cantidad == null) {
            addError("Debe ingresar fecha, hora y cantidad de personas");
            return;
        }

        Query queryMesas = XPersistence.getManager().createQuery(
                "SELECT m FROM Mesa m WHERE m.capacidad >= :cant AND m.estado <> :ocupada ORDER BY m.capacidad ASC"
        );
        queryMesas.setParameter("cant", cantidad);
        queryMesas.setParameter("ocupada", EstadoMesa.OCUPADA);

        List<Mesa> mesas = (List<Mesa>) queryMesas.getResultList();

        for (Mesa mesa : mesas) {

            Query queryReservas = XPersistence.getManager().createQuery(

                    "SELECT r FROM Reserva r WHERE r.mesa = :mesa AND r.fechaReserva = :fecha AND r.horaReserva = :hora AND r.estado <> :cancelada"
            );
            queryReservas.setParameter("mesa", mesa);
            queryReservas.setParameter("fecha", fecha);
            queryReservas.setParameter("hora", hora);
            queryReservas.setParameter("cancelada", EstadoReserva.CANCELADA);

            List<Reserva> reservas = (List<Reserva>) queryReservas.getResultList();


            if (reservas.isEmpty()) {

                mesa.setEstado(EstadoMesa.OCUPADA);
                XPersistence.getManager().merge(mesa);

                getView().setValue("mesa", mesa.getId());
                addMessage("Mesa asignada automaticamente: " + mesa.getNumero());

            }
        }

        addError("No hay mesas disponibles para esa fecha y hora");
    }
}












