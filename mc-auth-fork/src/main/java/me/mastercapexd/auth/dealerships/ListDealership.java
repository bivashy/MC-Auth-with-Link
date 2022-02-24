package me.mastercapexd.auth.dealerships;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface ListDealership<T> {
	List<T> getList();

	void add(T object);

	void remove(T object);

	Iterator<T> iterator();

	default Optional<T> findFirstByPredicate(Predicate<T> predicate) {
		return getList().stream().filter(predicate).findFirst();
	}
}
