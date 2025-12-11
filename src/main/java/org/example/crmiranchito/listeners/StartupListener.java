package org.example.crmiranchito.listeners;

import org.example.crmiranchito.job.QuartzScheduler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

            try {

                QuartzScheduler.iniciar();
                System.out.println("Quartz iniciado correctamente.");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        QuartzScheduler.detener();
        System.out.println("Quartz detenido correctamente.");
    }
}
