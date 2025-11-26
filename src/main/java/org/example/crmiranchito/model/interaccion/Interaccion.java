package org.example.crmiranchito.model.interaccion;


import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.enums.TipoInteraccion;
import org.example.crmiranchito.model.Auditable;
import org.example.crmiranchito.model.cliente.Cliente;
import org.openxava.annotations.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter

@View(members =
"Cliente { cliente } " +
"Canal { canal } " +
"Detalle {tipo; detalle } " +
"Fecha { fecha } ")

@Tabs(@Tab(properties = "cliente.nombre, canal.nombre, tipo, fecha"))
public class Interaccion extends Auditable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Required
    private Cliente cliente;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@Required
    //private CanalComunicacion canal;

    @Enumerated(EnumType.STRING)
    private TipoInteraccion tipo;

    @Stereotype("MEMO")
    private String mensaje;

    private LocalDateTime fecha = LocalDateTime.now();


}
