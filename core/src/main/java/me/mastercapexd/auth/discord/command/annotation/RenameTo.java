package me.mastercapexd.auth.discord.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Required and used in discord module. Used when we need to change {@link revxrsal.commands.annotation.Flag} or {@link revxrsal.commands.annotation.Named}
 * name to another.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RenameTo {
    String value();

    String type();
}
