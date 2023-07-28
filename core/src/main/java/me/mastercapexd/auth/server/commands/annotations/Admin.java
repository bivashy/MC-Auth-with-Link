package me.mastercapexd.auth.server.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import revxrsal.commands.annotation.DistributeOnMethods;

/**
 * This annotation marks commands as 'Admin'. Required for {@link me.mastercapexd.auth.server.commands.parameters.ArgumentAccount}, or similar arguments for
 * preventing
 * usage in non-admin commands. We cannot use admin-arguments without this annotation
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DistributeOnMethods
public @interface Admin {
}
