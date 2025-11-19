package org.example.crmiranchito.model.usuario;


import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.enums.RolUsuario;
import org.example.crmiranchito.model.Auditable;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Required;
import org.openxava.annotations.Tab;
import org.openxava.annotations.View;

import javax.persistence.*;

@Entity
@Getter
@Setter

@View(members =
"Datos Personales { nombre; username }" +
"Rol y Estado { rol; activo }")

@Tab(properties = "nombre, username, rol, activo")


public class Usuario extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Required
    private String nombre;

    @Required
    @Column(unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    @Required
    private RolUsuario rol;

    @Hidden
    private boolean activo = true;





}
