package me.mastercapexd.auth.shared.commands.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandCooldown {

    int DEFAULT_VALUE = 3000;


    long value();

    TimeUnit unit() default TimeUnit.MILLISECONDS;

}
