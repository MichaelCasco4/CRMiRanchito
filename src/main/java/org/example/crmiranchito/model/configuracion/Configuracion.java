package org.example.crmiranchito.model.configuracion;

import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.model.usuario.Usuario;
import org.openxava.annotations.*;

import javax.persistence.*;

@Entity
@Getter
@Setter

@View(members =
"Usuario { usuario } " +
"Credenciales { nuevoUsername; nuevoPassword } ")
@Tab(properties = "usuario.nombre, usuario.username")
public class Configuracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Required
    @DescriptionsList(descriptionProperties = "nombre, username, rol")
    private Usuario usuario;

    @Required
    private String nuevoUsername;

    @Required
    @Stereotype("Password")
    private String nuevoPassword;

}
