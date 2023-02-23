package me.mastercapexd.auth.bungee.scheduler;

import me.mastercapexd.auth.proxy.scheduler.ProxyScheduler;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class BungeeSchedulerWrapper implements ProxyScheduler {
    private final ScheduledTask scheduledTask;

    public BungeeSchedulerWrapper(ScheduledTask scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    @Override
    public void cancel() {
        scheduledTask.cancel();
    }
}
