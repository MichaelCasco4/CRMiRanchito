package org.example.crmiranchito.model.reportes;

import org.example.crmiranchito.model.reserva.Reserva;
import org.openxava.jpa.XPersistence;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReporteService {

    public static List<ItemEncuesta> obtenerPromediosEncuestas(LocalDate d, LocalDate h) {

        EntityManager em = XPersistence.getManager();
        List<ItemEncuesta> lista = new ArrayList<>();

        Double cg = em.createQuery(
                        "SELECT AVG(e.calificacionGeneral) FROM Reserva r JOIN r.encuestas e " +
                                "WHERE r.fechaReserva BETWEEN :d AND :h", Double.class)
                .setParameter("d", d)
                .setParameter("h", h)
                .getSingleResult();
        lista.add(new ItemEncuesta("Calificación General", cg));


        Double comida = em.createQuery(
                        "SELECT AVG(e.comida) FROM Reserva r JOIN r.encuestas e " +
                                "WHERE r.fechaReserva BETWEEN :d AND :h", Double.class)
                .setParameter("d", d)
                .setParameter("h", h)
                .getSingleResult();
        lista.add(new ItemEncuesta("Comida", comida));


        Double atencion = em.createQuery(
                        "SELECT AVG(e.atencion) FROM Reserva r JOIN r.encuestas e " +
                                "WHERE r.fechaReserva BETWEEN :d AND :h", Double.class)
                .setParameter("d", d)
                .setParameter("h", h)
                .getSingleResult();
        lista.add(new ItemEncuesta("Atención", atencion));


        Double ts = em.createQuery(
                        "SELECT AVG(e.tiempoServicio) FROM Reserva r JOIN r.encuestas e " +
                                "WHERE r.fechaReserva BETWEEN :d AND :h", Double.class)
                .setParameter("d", d)
                .setParameter("h", h)
                .getSingleResult();
        lista.add(new ItemEncuesta("Tiempo Servicio", ts));

        Double ambiente = em.createQuery(
                        "SELECT AVG(e.ambiente) FROM Reserva r JOIN r.encuestas e " +
                                "WHERE r.fechaReserva BETWEEN :d AND :h", Double.class)
                .setParameter("d", d)
                .setParameter("h", h)
                .getSingleResult();
        lista.add(new ItemEncuesta("Ambiente", ambiente));

        return lista;
    }

    public static List<ItemMesaUso> obtenerUsoMesas(LocalDate d, LocalDate h) {

        EntityManager em = XPersistence.getManager();

        return em.createQuery(
                        "SELECT new org.example.crmiranchito.model.reportes.ItemMesaUso(r.mesa.numero, COUNT(r)) " +
                                "FROM Reserva r " +
                                "WHERE r.fechaReserva BETWEEN :d AND :h " +
                                "GROUP BY r.mesa.numero " +
                                "ORDER BY COUNT(r) DESC",
                        ItemMesaUso.class)
                .setParameter("d", d)
                .setParameter("h", h)
                .getResultList();
    }
}
