package com.bivashy.auth.api.util;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;

// legally stolen from guava
public final class MemoizingSupplier<T> implements Supplier<T>, Serializable {
    private static final long serialVersionUID = 0;
    final Supplier<T> delegate;
    transient volatile boolean initialized;
    // "value" does not need to be volatile; visibility piggy-backs
    // on volatile read of "initialized".
    transient T value;

    MemoizingSupplier(Supplier<T> delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    public static <T> Supplier<T> memoize(Supplier<T> supplier) {
        return new MemoizingSupplier<>(supplier);
    }

    @Override
    public T get() {
        // A 2-field variant of Double Checked Locking.
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    T t = delegate.get();
                    value = t;
                    initialized = true;
                    return t;
                }
            }
        }
        return value;
    }

    @Override
    public String toString() {
        return "Suppliers.memoize(" + delegate + ")";
    }
}