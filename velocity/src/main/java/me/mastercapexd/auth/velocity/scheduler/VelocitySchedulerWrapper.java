package me.mastercapexd.auth.velocity.scheduler;

import com.bivashy.auth.api.server.scheduler.ServerScheduler;
import com.velocitypowered.api.scheduler.ScheduledTask;

public class VelocitySchedulerWrapper implements ServerScheduler {
    private final ScheduledTask scheduledTask;

    public VelocitySchedulerWrapper(ScheduledTask scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    @Override
    public void cancel() {
        scheduledTask.cancel();
    }
}
