package me.mastercapexd.auth.proxy.commands.annotations;

import revxrsal.commands.annotation.DistributeOnMethods;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@DistributeOnMethods
@Retention(RUNTIME)
@Target(TYPE)
public @interface Permission {
    String value();
}
