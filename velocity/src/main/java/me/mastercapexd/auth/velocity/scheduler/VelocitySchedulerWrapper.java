package me.mastercapexd.auth.velocity.scheduler;

import com.velocitypowered.api.scheduler.ScheduledTask;

import me.mastercapexd.auth.proxy.scheduler.ProxyScheduler;

public class VelocitySchedulerWrapper implements ProxyScheduler {
    private final ScheduledTask scheduledTask;

    public VelocitySchedulerWrapper(ScheduledTask scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    @Override
    public void cancel() {
        scheduledTask.cancel();
    }
}
