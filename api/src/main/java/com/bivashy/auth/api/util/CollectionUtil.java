package com.bivashy.auth.api.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionUtil {
    private CollectionUtil() {
    }

    public static <T> T[] addAll(T[] firstArray, T[] secondArray) {
        T[] both = Arrays.copyOf(firstArray, firstArray.length + secondArray.length);
        System.arraycopy(secondArray, 0, both, firstArray.length, secondArray.length);
        return both;
    }

    public static class ArrayPairHashMapAdapter<T> extends HashMap<T, T> {
        public ArrayPairHashMapAdapter(T[] values) {
            if (values.length % 2 != 0)
                throw new IllegalArgumentException("Argument count must be even, but got " + values.length);
            for (int i = 0; i < values.length; i += 2)
                put(values[i], values[i + 1]);
        }

        public static class PaginatedList<T> extends ArrayList<T> {
            private final long onePageSize;

            public PaginatedList(long onePageSize, Collection<T> wrapping) {
                super(wrapping);
                this.onePageSize = onePageSize;
            }

            public List<T> getPage(long page) {
                return stream().skip((page - 1) * onePageSize).limit(onePageSize).collect(Collectors.toList());
            }
        }
    }
}
