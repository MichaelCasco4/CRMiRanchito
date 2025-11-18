package org.example.crmiranchito.model.reserva;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.crmiranchito.enums.AreaMesa;
import org.example.crmiranchito.enums.EstadoMesa;
import org.example.crmiranchito.model.Auditable;
import org.openxava.annotations.Required;
import org.openxava.annotations.Tab;
import org.openxava.annotations.Tabs;
import org.openxava.annotations.View;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mesa")
@View(members = " Mesa { numero; capacidad; area; estado }")
@Tabs(@Tab(properties = "numero, capacidad, area, estado"))


public class Mesa extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Required
    @Column(nullable = false, unique = true)
    private String numero;

    @Required
    @Column(nullable = false)
    private Integer capacidad;

    @Required
    @Enumerated(EnumType.STRING)
    private AreaMesa area;

    @Required
    @Enumerated(EnumType.STRING)
    private EstadoMesa estado;

    @OneToMany(mappedBy = "mesa", fetch = FetchType.LAZY)
    private List<Reserva> reservas;
}
