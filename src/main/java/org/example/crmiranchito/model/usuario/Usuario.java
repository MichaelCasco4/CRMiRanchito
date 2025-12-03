package org.example.crmiranchito.model.usuario;


import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.enums.RolUsuario;
import org.example.crmiranchito.model.Auditable;
import org.openxava.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter

@View(members = "nombre; username; password; rol; activo")
@Tab(properties = "nombre, username, rol, activo")

public class Usuario extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Required
    @Stereotype("Password")
    @NotBlank
    private String password;

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
