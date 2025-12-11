package org.example.crmiranchito.actions;

import org.openxava.actions.SaveAction;

public class CargarGraficosReporteAction extends SaveAction {

    @Override
    public void execute() throws Exception{

        super.execute();
        addMessage("Graficos Actualizados correctamente");

    }

}
