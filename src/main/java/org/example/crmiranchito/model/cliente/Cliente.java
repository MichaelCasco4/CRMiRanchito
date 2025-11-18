package org.example.crmiranchito.model.cliente;


import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.model.Auditable;
import org.example.crmiranchito.model.interaccion.Interaccion;
import org.example.crmiranchito.model.promocion.Promocion;
import org.example.crmiranchito.model.reserva.Reserva;
import org.openxava.annotations.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter

@Tabs(@Tab(properties = "nombre, email, telefono"))
@View(members = "Datos Personales { nombre; email; telefono; preferencias }")

public class Cliente extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Required
    private String nombre;

    @Required
    @Stereotype("Telephone")
    private  String telefono;

    @Required
    @Stereotype("Email")
    private String correo;

    @Temporal(TemporalType.DATE)
    private java.util.Date fechaNacimiento;

    @Stereotype("MEMO")
    private String preferencias;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Interaccion> interacciones;

    @ManyToMany(mappedBy = "clientes", fetch = FetchType.LAZY)
    private List<Promocion> promociones;

    @Column(length= 20)
    private String estado;

}
