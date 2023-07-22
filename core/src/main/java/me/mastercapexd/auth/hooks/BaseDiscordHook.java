package me.mastercapexd.auth.hooks;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class BaseDiscordHook implements DiscordHook {
    private JDA jda;

    @Override
    public JDA getJDA() {
        return jda;
    }

    public CompletableFuture<JDA> initialize(Consumer<JDABuilder> consumer) {
        return CompletableFuture.supplyAsync(() -> {
            JDABuilder jdaBuilder = JDABuilder.createDefault(PLUGIN.getConfig().getDiscordSettings().getBotToken());
            consumer.accept(jdaBuilder);
            try {
                jda = jdaBuilder.build().awaitReady();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            return jda;
        }).handle((ignored, ex) -> {
            if (ex != null)
                ex.printStackTrace();
            return ignored;
        });
    }
}
