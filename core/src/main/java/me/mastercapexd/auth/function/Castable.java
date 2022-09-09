package me.mastercapexd.auth.function;

import java.util.Optional;

public interface Castable<T> {
    /**
     * Casts this object to the provided one. Useful for interfaces or abstract
     * classes. Method may throw {@link ClassCastException}
     *
     * @param <R>.   Object that you provided
     * @param clazz. R class that will cast this object
     * @return casted object
     */
    default <R extends T> R as(Class<R> clazz) {
        return clazz.cast(this);
    }

    /**
     * Casts this to the provided one. Useful for interfaces or abstract classes.
     * Method will return {@linkplain Optional#empty()} if cannot cast this object
     *
     * @param <R>.   Object that you provided
     * @param clazz. R class that will cast this object
     * @return Optional value of casted object
     */
    default <R extends T> Optional<R> safeAs(Class<R> clazz) {
        return Optional.ofNullable(safeAs(clazz, null));
    }

    /**
     * Casts this to the provided one. Useful for interfaces or abstract classes.
     * Method will return default if cannot cast this object
     *
     * @param <R>.     Object that you provided
     * @param clazz.   R class that will cast this object
     * @param default. Default value if cannot cast object.
     * @return Optional value of casted object
     */
    default <R extends T> R safeAs(Class<R> clazz, R defaultValue) {
        try {
            return clazz.cast(this);
        } catch(ClassCastException ignored) {
            return defaultValue;
        }
    }
}
