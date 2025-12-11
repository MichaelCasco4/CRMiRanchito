package org.example.crmiranchito.job;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;


public class QuartzScheduler {

    private static Scheduler scheduler;

    public static void iniciar(){
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();

            JobDetail job = JobBuilder
                    .newJob(ReservaExpirationJob.class)
                    .withIdentity("reservaExpirationJob", "grupo1")
                    .build();

            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("triggerReserva", "grupo1" )
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInMinutes(1)
                            .repeatForever()).build();

            scheduler.scheduleJob(job, trigger);
            scheduler.start();


        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public static void detener(){

        try {

            if(scheduler != null && !scheduler.isShutdown()) {

                scheduler.shutdown(true);
                System.out.println("Quartz Scheduler Detenido correctamente.");

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
