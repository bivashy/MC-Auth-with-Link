package me.mastercapexd.auth.messenger.commands.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
/**
 * Required and used in discord module. Used when we need to change {@link revxrsal.commands.annotation.Flag} or {@link revxrsal.commands.annotation.Named}
 * name to another.
 */ public @interface RenameTo {
    String value();

    String type();
}
