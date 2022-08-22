package me.mastercapexd.auth.utils;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class CollectionUtils {
    private CollectionUtils() {}

    public static <T> List<List<T>> chopList(List<T> list, final int newListSize) {
        List<List<T>> parts = new ArrayList<>();
        final int oldListSize = list.size();
        for (int i = 0; i < oldListSize; i += newListSize)
            parts.add(new ArrayList<T>(list.subList(i, Math.min(oldListSize, i + newListSize))));

        return parts;
    }

    /**
     * Returns paginated list of the provided list.
     *
     * @param source.      List that need to be cut off
     * @param page.        List page (Starts from 1)
     * @param onePageSize. One page size
     * @return
     */
    public static <T> List<T> getListPage(List<T> source, int page, int onePageSize) {
        return source.stream().skip((page - 1) * onePageSize).limit(onePageSize).collect(Collectors.toList());
    }

    public static Map<String, String> createStringMap(String... array) {
        if (array.length % 2 != 0)
            throw new IllegalArgumentException("Argument count must be even, but got " + array.length);
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < array.length; i += 2)
            map.put(array[i], array[i + 1]);
        return map;
    }

    public static <T> T[] addAll(T[] firstArray, T[] secondArray) {
        T[] both = Arrays.copyOf(firstArray, firstArray.length + secondArray.length);
        System.arraycopy(secondArray, 0, both, firstArray.length, secondArray.length);
        return both;
    }

    public static int getMaxPages(int totalItemCount, int onePageLimit) {
        return (int) Math.ceil((float) totalItemCount / onePageLimit);
    }

    public static <K, V> Entry<K, V> newEntry(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }
}
