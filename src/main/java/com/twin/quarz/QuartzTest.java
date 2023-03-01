package com.twin.quarz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class QuartzTest {
    public static void main(String[] args) throws Exception {

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        JobDetail jobDetail = newJob(TestJob.class).withIdentity("jobname1", "jobgroup1").build();
        SimpleTrigger trigger = newTrigger().withIdentity("triggerName1", "triggerGroup1")
                .startNow()
                .withSchedule(simpleSchedule().withIntervalInSeconds(2).repeatForever())
                .build();
        scheduler.scheduleJob(jobDetail,trigger);
    }
}
