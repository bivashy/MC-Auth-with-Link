package me.mastercapexd.auth.proxy.commands.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import revxrsal.commands.annotation.DistributeOnMethods;

@DistributeOnMethods
@Retention(RUNTIME)
@Target(TYPE)
public @interface Permission {
    String value();
}
