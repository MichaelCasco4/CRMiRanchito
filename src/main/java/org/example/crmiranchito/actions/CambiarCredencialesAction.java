package org.example.crmiranchito.actions;

import org.example.crmiranchito.model.usuario.Usuario;
import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;


public class CambiarCredencialesAction extends ViewBaseAction{

    @Override
    public void execute() throws Exception {

        Usuario usuario = (Usuario)getView().getValue("usuario");
        String nuevoUsername = (String)getView().getValue("nuevoUsername");
        String nuevoPassword = (String)getView().getValue("nuevoPassword");

        if(usuario != null) {

            usuario.setUsername(nuevoUsername);
            usuario.setPassword(nuevoPassword);
            XPersistence.getManager().merge(usuario);
            addMessage("Credenciales actualizadas correctamente");
        }else  {
            addMessage("Usuario no encontrado");
        }
    }
}
