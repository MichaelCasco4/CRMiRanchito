package org.example.crmiranchito.actions;

import org.openxava.actions.ViewBaseAction;



public class CambiarCredencialesAction extends ViewBaseAction{

    @Override
    public void execute() throws Exception {

        if (getView().isKeyEditable()) {
            getView().findObject();
            getView().setKeyEditable(false);
        }


        getView().setViewName("CambiarCredenciales");
        showDialog();
    }
}
