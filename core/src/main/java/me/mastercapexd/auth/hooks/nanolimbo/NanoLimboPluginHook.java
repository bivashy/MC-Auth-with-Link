package me.mastercapexd.auth.hooks.nanolimbo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.IntStream;

import com.bivashy.auth.api.hook.LimboPluginHook;

import ua.nanit.limbo.server.Command;
import ua.nanit.limbo.server.CommandHandler;
import ua.nanit.limbo.server.LimboServer;
import ua.nanit.limbo.server.data.InfoForwarding;

public interface NanoLimboPluginHook extends LimboPluginHook {

    CommandHandler<Command> DUMMY_COMMAND_HANDLER = new CommandHandler<Command>() {
        @Override
        public Collection<Command> getCommands() {
            return Collections.emptyList();
        }

        @Override
        public void register(Command command) {
        }

        @Override
        public boolean executeCommand(String s) {
            return false;
        }
    };
    InfoForwardingFactory FORWARDING_FACTORY = new InfoForwardingFactory();

    InfoForwarding createForwarding();

    ClassLoader classLoader();

    default LimboServer createLimboServer(SocketAddress address) {
        return new LimboServer(new NanoLimboConfig(address, createForwarding()), DUMMY_COMMAND_HANDLER, classLoader());
    }

    default Optional<InetSocketAddress> findAvailableAddress(IntStream portRange) {
        return portRange.filter(port -> {
            try (ServerSocket ignored = new ServerSocket(port)) {
                return true;
            } catch (IOException ignored) {
                return false;
            }
        }).mapToObj(InetSocketAddress::new).findFirst();
    }

}
