package com.bivashy.auth.api.util;

/**
 * A functional interface that represents a function that takes in a parameter of type T and returns a result of type V,
 * and throws an exception of type {@link E}.
 *
 * @param <T> the type of the input to the function
 * @param <V> the type of the result of the function
 * @param <E> the type of the exception that can be thrown
 * @see java.util.function.Function
 */
public interface ThrowableFunction<T, V, E extends Throwable> {

    /**
     * Applies the given function to the specified argument.
     *
     * @param t the argument to apply the function to
     * @return the result of applying the function to the argument
     * @throws E if an error occurs during the application of the function
     */
    V apply(T t) throws E;

}
