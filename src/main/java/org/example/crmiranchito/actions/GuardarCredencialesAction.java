package org.example.crmiranchito.actions;

import org.example.crmiranchito.model.usuario.Usuario;
import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;


import javax.persistence.Query;

public class GuardarCredencialesAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception{

        String nuevoUsername = (String) getView().getValue("nuevoUsername");
        String nuevoPassword = (String) getView().getValue("nuevoPassword");
        String confirmar = (String) getView().getValue("confirmarPassword");


        if(nuevoUsername==null || nuevoUsername.isBlank()){

            addError("El username no puede estar vacio");
            return;

        }

        if(nuevoPassword==null || nuevoPassword.isBlank()){

            addError("La contraseña no puede estar vacio");
            return;
        }

        if(!nuevoPassword.equals(confirmar)){
            addError("Las contraseñas no coinciden");
            return;
        }

        Long usuarioId = (Long) getView().getValue("id");
        Usuario usuario = XPersistence.getManager().find(Usuario.class, usuarioId);

        Query q = XPersistence.getManager().createQuery(
                "SELECT count(u) FROM Usuario u WHERE u.username = :username AND u.id <> :id"
        );
        q.setParameter("username", nuevoUsername);
        q.setParameter("id", usuarioId);

        Long count = (Long) q.getSingleResult();
        if(count>0){

            addError("El usuario ya existe");
            return;

        }

        usuario.setUsername(nuevoUsername);
        usuario.setPassword(nuevoPassword);

        XPersistence.getManager().merge(usuario);

        closeDialog();
        addMessage("Credenciales guardadas correctamente");


    }
}
