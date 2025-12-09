package org.example.crmiranchito.model.cliente;


import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.enums.EstadoCliente;
import org.example.crmiranchito.model.Auditable;
import org.example.crmiranchito.model.interaccion.Interacion;
import org.openxava.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;

@Entity
@Getter
@Setter

@View(members =
"Datos Personales { nombre; correo; telefono; fechaNacimiento; estado; preferencias } ")

@Tab(properties = "nombre, correo, telefono, estado")

public class Cliente extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @Column(length = 20)
    @Required
    private EstadoCliente estado = EstadoCliente.ACTIVO;

    @OneToMany(mappedBy = "cliente")
    @ListProperties("fechaHora, canal, asunto, usuario.username")
    private Collection<Interacion> interaciones;

    @AssertTrue(message = "La fecha de nacimiento no puede ser en el futuro")
    private boolean isFechaNacimientoValida(){
        if(fechaNacimiento == null)
            return true;
        LocalDate fecha = fechaNacimiento.toInstant()
                .atZone(ZoneId.systemDefault()).
                toLocalDate();
        return !fecha.isAfter(LocalDate.now());
    }


}
