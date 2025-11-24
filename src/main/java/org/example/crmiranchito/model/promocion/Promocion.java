package org.example.crmiranchito.model.promocion;


import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.model.Auditable;
import org.example.crmiranchito.model.cliente.Cliente;
import org.openxava.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter

@View(members =
"Datos { titulo; descripcion; imagen } " +
"Vigencia { fechaInicio; fechaFin } " )

@Tab(properties = "titulo, fechaInicio, fechaFin")

public class Promocion extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Required
    @Column(length = 100)
    private String titulo;

    @Stereotype("MEMO")
    private String descripcion;

    @Stereotype("PHOTO")
    private byte[] imagen;

    @Required
    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "cliente_Promocion",
    joinColumns = @JoinColumn(name = "promocion_id"),
    inverseJoinColumns = @JoinColumn(name = "cliente_id")
    )


    @ListProperties("nombre, telefono, correo, estado")
    private List<Cliente> clientes;

    @AssertTrue( message = "La fecha fin debe ser posterior a la fecha de inicio")
    private boolean isFechasValidas() {
        return fechaInicio == null || fechaFin == null || fechaFin.isBefore(fechaInicio);

    }

}
