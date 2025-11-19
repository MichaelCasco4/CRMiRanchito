package org.example.crmiranchito.model.cliente;


import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.enums.EstadoCliente;
import org.example.crmiranchito.model.Auditable;
import org.example.crmiranchito.model.interaccion.Interaccion;
import org.example.crmiranchito.model.promocion.Promocion;
import org.example.crmiranchito.model.reserva.Reserva;
import org.openxava.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Entity
@Getter
@Setter

@Tab(properties = "nombre, correo, telefono, estado")
@View(members =
"Datos Personales { nombre; correo; telefono; fechaNacimiento; preferencias } " +
"Estado { estado } " )

public class Cliente extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Required
    @Column(length = 80)
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

    @Enumerated(EnumType.STRING)
    private EstadoCliente estado = EstadoCliente.ACTIVO;

    @AssertTrue(message = "La fecha de nacimiento no puede ser en el futuro")
    private boolean isFechaNacimientoValida(){

        if(fechaNacimiento == null)
            return true;
        LocalDate fecha = fechaNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return !fecha.isAfter(LocalDate.now());
    }


}
