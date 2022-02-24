package me.mastercapexd.auth.utils;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class CollectionUtils {
	public static <T> List<List<T>> chopList(List<T> list, final int newListSize) {
		List<List<T>> parts = new ArrayList<List<T>>();
		final int oldListSize = list.size();
		for (int i = 0; i < oldListSize; i += newListSize) 
			parts.add(new ArrayList<T>(list.subList(i, Math.min(oldListSize, i + newListSize))));
		
		return parts;
	}

	public static <T> List<T> getListPage(List<T> source, int page, int count) {
		List<T> localpage = new ArrayList<>();
		int to = (page * count - 1);
		int from = (to - count);
		for (int i = to; i > from; i--)
			try {
				localpage.add(source.get(i));
			} catch (Exception e) {
			}

		return localpage;
	}

	public static int getMaxPages(int totalItemCount, int onePageLimit) {
		return (int) Math.ceil((float)totalItemCount/onePageLimit);
	}
	
	public static <K,V> Entry<K, V> newEntry(K key,V value){
		return new AbstractMap.SimpleEntry<>(key,value);
	}
}
