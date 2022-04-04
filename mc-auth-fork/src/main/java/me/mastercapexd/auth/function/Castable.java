package me.mastercapexd.auth.function;

public interface Castable<T> {
	default <R extends T> R as(Class<R> clazz) {
		return clazz.cast(this);
	}
}
