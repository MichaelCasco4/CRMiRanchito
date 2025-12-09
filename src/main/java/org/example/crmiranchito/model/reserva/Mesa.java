package org.example.crmiranchito.model.reserva;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.crmiranchito.enums.AreaMesa;
import org.example.crmiranchito.enums.EstadoMesa;
import org.example.crmiranchito.model.Auditable;
import org.openxava.annotations.*;
import org.openxava.jpa.XPersistence;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "Mesa")

@Tab(properties = "numero, capacidad, area, estado")

@View(members =
"Datos Mesa { numero; capacidad; area; estado } " )

public class Mesa extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Required
    @Column(nullable = false, unique = true)
    private String numero;

    @Required
    private Integer capacidad;

    @Required
    @Enumerated(EnumType.STRING)
    private AreaMesa area;

    @ReadOnly
    @Transient
    public EstadoMesa getEstado() {

        Reserva r = obtenerReservaActual();
        if(r == null)
            return EstadoMesa.DISPONIBLE;

        LocalDateTime inicio = LocalDateTime.of(r.getFechaReserva(), r.getHoraReserva());
        LocalDateTime fin = inicio.plusMinutes(r.getDuracionMinutos());
        LocalDateTime ahora = LocalDateTime.now();

        if(ahora.isAfter(fin))
            return EstadoMesa.DISPONIBLE;

        if(!ahora.isBefore(inicio) && ahora.isBefore(fin))
            return EstadoMesa.OCUPADA;

        if(ahora.isBefore(inicio))
            return EstadoMesa.RESERVADA;

        return EstadoMesa.DISPONIBLE;
    };

    private Reserva obtenerReservaActual() {

        List<Reserva> reservas = XPersistence.getManager().createQuery(

                "SELECT r FROM Reserva r WHERE r.mesa = :mesa AND r.estado NOT IN ('CANCELADA', 'TERMINADA') ORDER BY r.fechaReserva ASC, r.horaReserva ASC", Reserva.class)
                .setParameter("mesa", this)
                .getResultList();

        return reservas.isEmpty() ? null : reservas.get(0);

    }

}
