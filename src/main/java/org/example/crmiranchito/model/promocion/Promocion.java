package org.example.crmiranchito.model.promocion;


import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.model.Auditable;
import org.example.crmiranchito.model.cliente.Cliente;
import org.openxava.annotations.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter

@View(members =
"Datos { nombre; descripcion } " +
"Vigencia { fechaInicio; fechaFin } " +
"Clientes { clientes } ")

@Tabs(@Tab(properties = "nombre, fechaInicio, fechaFin"))
public class Promocion extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Required
    private String titulo;

    @Stereotype("MEMO")
    private String descripcion;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "promocion_cliente",
    joinColumns = @JoinColumn(name = "promocion_id"),
    inverseJoinColumns = @JoinColumn(name = "cliente_id"))
    private List<Cliente> clientes;
}
