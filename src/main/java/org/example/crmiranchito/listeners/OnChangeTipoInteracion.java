package org.example.crmiranchito.listeners;

import org.example.crmiranchito.enums.TipoInteraccion;
import org.openxava.actions.OnChangePropertyBaseAction;

public class OnChangeTipoInteracion extends OnChangePropertyBaseAction {

    @Override
    public void execute() throws Exception {

        TipoInteraccion tipo = (TipoInteraccion) getNewValue();

        boolean mostrar =
                tipo == TipoInteraccion.CONSULTA ||
                        tipo == TipoInteraccion.RECLAMO ||
                        tipo == TipoInteraccion.SATISFACCION;
        getView().setHidden("comentario", !mostrar);

        if(!mostrar) {

            getView().setValue("comentario", null);

        }

    }

}
