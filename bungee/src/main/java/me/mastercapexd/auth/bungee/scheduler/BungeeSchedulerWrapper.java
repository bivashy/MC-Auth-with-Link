package me.mastercapexd.auth.bungee.scheduler;

import com.bivashy.auth.api.server.scheduler.ServerScheduler;

import net.md_5.bungee.api.scheduler.ScheduledTask;

public class BungeeSchedulerWrapper implements ServerScheduler {
    private final ScheduledTask scheduledTask;

    public BungeeSchedulerWrapper(ScheduledTask scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    @Override
    public void cancel() {
        scheduledTask.cancel();
    }
}
