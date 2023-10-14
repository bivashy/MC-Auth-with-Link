package me.mastercapexd.auth.shared.commands.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import com.bivashy.auth.api.config.message.Messages;

import me.mastercapexd.auth.shared.commands.annotation.CommandCooldown;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.process.CommandCondition;

public class CommandCooldownCondition<T> implements CommandCondition {

    private final Map<Integer, Long> cooldownMap = new HashMap<>();
    private final Messages<T> messages;
    private final Function<T, RuntimeException> messageException;

    public CommandCooldownCondition(Messages<T> messages, Function<T, RuntimeException> messageException) {
        this.messages = messages;
        this.messageException = messageException;
    }

    @Override
    public void test(@NotNull CommandActor commandActor, @NotNull ExecutableCommand executableCommand, @NotNull @Unmodifiable List<String> list) {
        if (!executableCommand.hasAnnotation(CommandCooldown.class))
            return;

        CommandCooldown commandCooldown = executableCommand.getAnnotation(CommandCooldown.class);
        long cooldownInMillis = commandCooldown.unit().toMillis(commandCooldown.value());

        if (!cooldownMap.containsKey(executableCommand.getId())) {
            cooldownMap.putIfAbsent(executableCommand.getId(), System.currentTimeMillis());
            return;
        }

        long timestampDelta = System.currentTimeMillis() - cooldownMap.get(executableCommand.getId());
        if (timestampDelta < cooldownInMillis)
            throw messageException.apply(messages.getMessage("command-cooldown"));
        cooldownMap.put(executableCommand.getId(), System.currentTimeMillis());
    }

}
