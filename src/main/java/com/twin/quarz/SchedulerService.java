package com.twin.quarz;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;


@Service
public class SchedulerService {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private Scheduler scheduler;

    public Scheduler getScheduler() {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        return scheduler;
    }

    public void addJob(QuartzBean job) throws Exception {
        JobKey jobKey = new JobKey(job.getJobName(), job.getJobGroupName());
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getTriggerName(), job.getTriggerGroup());
        boolean exists = scheduler.checkExists(jobKey);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (exists && trigger != null) {
            return;
        }
        if (exists) {
            if (trigger == null) {
                trigger = getTrigger(job.getCronExp(), jobKey, triggerKey);
                scheduler.scheduleJob(trigger);
            }
        } else {
            JobDetail jobDetail = newJob(TestJob.class).withIdentity(jobKey).build();
            if (null != trigger) {
                trigger = getTrigger(job.getCronExp(), jobKey, triggerKey);
                scheduler.rescheduleJob(triggerKey, trigger);
            } else {
                trigger = getTrigger(job.getCronExp(), jobKey, triggerKey);
                scheduler.scheduleJob(jobDetail, trigger);
            }
        }

    }

    private static Trigger getTrigger(String cronExp, JobKey jobKey, TriggerKey triggerKey) {
        return newTrigger().withIdentity(triggerKey).startNow().forJob(jobKey).withSchedule(CronScheduleBuilder.cronSchedule(cronExp)).build();
    }

    public List<QuartzBean> getJobs() throws Exception {
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<QuartzBean> objects = new ArrayList<>();
        for (JobKey jobKey : jobKeys) {
           QuartzBean quartzBean = new QuartzBean();
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            quartzBean.setJobName(jobKey.getName());
            quartzBean.setJobGroupName(jobKey.getGroup());
            for (Trigger trigger : triggers) {
                Trigger.TriggerState state = scheduler.getTriggerState(trigger.getKey());
                quartzBean.setState(state.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String expression = cronTrigger.getCronExpression();
                    quartzBean.setCronExp(expression);
                }
                TriggerKey key = trigger.getKey();
                String name = key.getName();
                String group = key.getGroup();
                quartzBean.setTriggerName(name);
                quartzBean.setTriggerGroup(group);
            }
            objects.add(quartzBean);
        }
        return objects;
    }
    //停止 pauseJob
    //恢复 resumeJob
    //删除 delete
    //立即执行 start
}
