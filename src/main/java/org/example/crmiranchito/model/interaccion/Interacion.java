package org.example.crmiranchito.model.interaccion;

import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.enums.CanalComunicacion;
import org.example.crmiranchito.model.Auditable;
import org.example.crmiranchito.model.cliente.Cliente;
import org.example.crmiranchito.model.reserva.Reserva;
import org.example.crmiranchito.model.usuario.Usuario;
import org.openxava.annotations.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter

@View(members =
"Datos del Cliente { cliente; reserva } " +
"Interacion { fechaHora; canal; asunto; descripcion } " +
"Responsable { usuario }")

@Tab(properties = "cliente.nombre, reserva.id, fechaHora, canal, asunto, usuario.username")

public class Interacion extends Auditable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Required
    @DescriptionsList(descriptionProperties = "nombre, telefono")
    private Cliente cliente;


    @ManyToOne(fetch = FetchType.LAZY)
    @DescriptionsList(descriptionProperties = "id, cliente.nombre, fechaReserva, horaReserva, estado")
    private Reserva reserva;

    @Required
    private LocalDateTime fechaHora = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Required
    private CanalComunicacion canal;

    @Required
    @Column(length = 100)
    private String asunto;

    @Stereotype("MEMO")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @DescriptionsList(descriptionProperties = "username")
    private Usuario usuario;




}
