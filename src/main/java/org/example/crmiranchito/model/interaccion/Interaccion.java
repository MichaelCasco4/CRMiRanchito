package org.example.crmiranchito.model.interaccion;


import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.enums.TipoInteraccion;
import org.example.crmiranchito.model.Auditable;
import org.example.crmiranchito.model.cliente.Cliente;
import org.example.crmiranchito.model.reserva.Reserva;
import org.openxava.annotations.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter

@View(members =
"Cliente { cliente } " +
"Canal { canal } " +
"Detalle {tipo; detalle } " +
"Fecha { fecha } " +
"\n" +
"Historial de Reservas del Cliente { reservasCliente }")

@Tabs(@Tab(properties = "cliente.nombre, tipo, fecha"))
public class Interaccion extends Auditable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Required
    @DescriptionsList(descriptionProperties = "nombre")
    private Cliente cliente;


    @OneToMany(mappedBy = "cliente")
    @ReadOnly
    @ListProperties(
            "fechaReserva, horaReserva, mesa.numero, estado, " +
                    "encuestas.promedio"
    )
    private List<Reserva> reservasCliente;

    @Enumerated(EnumType.STRING)
    private TipoInteraccion tipo;

    @Stereotype("MEMO")
    private String mensaje;

    private LocalDateTime fecha = LocalDateTime.now();


}
