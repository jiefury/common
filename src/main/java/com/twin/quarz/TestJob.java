package com.twin.quarz;

import org.quartz.*;

import java.util.Date;

public class TestJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Date date = new Date();
        Trigger trigger = jobExecutionContext.getTrigger();
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobKey key = jobDetail.getKey();
        String name = key.getName();
        TriggerKey triggerKey = trigger.getKey();
        System.out.println(name + " | " + "triggerKey:" + triggerKey + " hello job------------ - " + date);
    }
}
