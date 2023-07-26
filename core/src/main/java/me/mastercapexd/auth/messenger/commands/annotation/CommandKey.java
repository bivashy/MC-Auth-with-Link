package me.mastercapexd.auth.messenger.commands.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import revxrsal.commands.annotation.DistributeOnMethods;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@DistributeOnMethods
public @interface CommandKey {
    String value();
}
