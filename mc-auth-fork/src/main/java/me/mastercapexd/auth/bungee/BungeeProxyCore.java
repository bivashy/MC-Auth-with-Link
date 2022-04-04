package me.mastercapexd.auth.bungee;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import me.mastercapexd.auth.bungee.api.bossbar.BungeeProxyBossbar;
import me.mastercapexd.auth.bungee.api.title.BungeeProxyTitle;
import me.mastercapexd.auth.bungee.player.BungeeProxyPlayer.BungeeProxyPlayerFactory;
import me.mastercapexd.auth.bungee.server.BungeeServer;
import me.mastercapexd.auth.proxy.ProxyCore;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.api.bossbar.ProxyBossbar;
import me.mastercapexd.auth.proxy.api.title.ProxyTitle;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.proxy.server.Server;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public enum BungeeProxyCore implements ProxyCore {
	INSTANCE;

	private static final ProxyServer PROXY_SERVER = ProxyServer.getInstance();
	private static final ExecutorService EXECUTOR_SERVICE = Executors.newScheduledThreadPool(20);

	@Override
	public <E> void callEvent(E event) {
		PROXY_SERVER.getPluginManager().callEvent((Event) event);
	}

	@Override
	public ProxyPlayer getPlayer(UUID uniqueId) {
		return BungeeProxyPlayerFactory.wrapPlayer(PROXY_SERVER.getPlayer(uniqueId));
	}

	@Override
	public ProxyPlayer getPlayer(String name) {
		return BungeeProxyPlayerFactory.wrapPlayer(PROXY_SERVER.getPlayer(name));
	}

	@Override
	public Logger getLogger() {
		return PROXY_SERVER.getLogger();
	}

	@Override
	public ProxyTitle createTitle(String title) {
		return new BungeeProxyTitle();
	}

	@Override
	public ProxyBossbar createBossbar(String title) {
		return new BungeeProxyBossbar(title);
	}

	@Override
	public Server serverFromName(String serverName) {
		return new BungeeServer(PROXY_SERVER.getServerInfo(serverName));
	}

	@Override
	public void registerListener(ProxyPlugin plugin, Object listener) {
		PROXY_SERVER.getPluginManager().registerListener(plugin.as(AuthPlugin.class), (Listener) listener);
	}

	@Override
	public void schedule(ProxyPlugin plugin, Runnable task, long delay, long period, TimeUnit unit) {
		PROXY_SERVER.getScheduler().schedule(plugin.as(AuthPlugin.class), task, delay, period, unit);
	}

	@Override
	public void runAsync(Runnable task) {
		EXECUTOR_SERVICE.execute(task);
	}

}
