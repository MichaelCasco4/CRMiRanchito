package org.example.crmiranchito.run;

import org.example.crmiranchito.scheduler.EstadoMesaSch;
import org.openxava.util.*;

/**
 * Execute this class to start the application.
 a*
 * With OpenXava Studio/Eclipse: Right mouse button > Run As > Java Application
 */

public class CRM {

	public static void main(String[] args) throws Exception {
		//DBServer.start("CRM-db"); // To use your own database comment this line and configure src/main/webapp/META-INF/context.xml
		AppServer.run("CRM"); // Use AppServer.run("") to run in root context

        new EstadoMesaSch();
	}

}
