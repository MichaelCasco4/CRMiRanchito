package org.example.crmiranchito.actions;

import org.example.crmiranchito.model.encuesta.ReporteSatisfaccion;
import org.openxava.actions.ViewBaseAction;
import org.openxava.model.MapFacade;

import java.util.Map;

public class CargarGraficosReporteAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {

        Map key = getView().getKeyValues();

        ReporteSatisfaccion r =
                (ReporteSatisfaccion) MapFacade.findEntity("ReporteSatisfaccion", key);

        r.cargarGraficos();

        getView().refresh();
        addMessage("Gráficos actualizados correctamente");
    }
}


