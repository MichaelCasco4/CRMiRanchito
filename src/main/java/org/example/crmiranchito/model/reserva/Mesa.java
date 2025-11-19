package org.example.crmiranchito.model.reserva;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.crmiranchito.enums.AreaMesa;
import org.example.crmiranchito.enums.EstadoMesa;
import org.example.crmiranchito.model.Auditable;
import org.openxava.annotations.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Tab(properties = "numero, capacidad, area, estado")
@View(members =
"Datos Mesa { numero; capacidad; area; estado } " +
"Reservas { reservas } ")


public class Mesa extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Required
    @Column(nullable = false, unique = true)
    private String numero;

    @Required
    private Integer capacidad;

    @Required
    @Enumerated(EnumType.STRING)
    private AreaMesa area;

    @Required
    @Enumerated(EnumType.STRING)
    private EstadoMesa estado;

    @OneToMany(mappedBy = "mesa", fetch = FetchType.LAZY)
    @ListProperties("fechaReserva, horaReserva, cliente.nombre, estado")
    private List<Reserva> reservas;
}
