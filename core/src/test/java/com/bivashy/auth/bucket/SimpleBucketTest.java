package com.bivashy.auth.bucket;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import org.junit.jupiter.api.*;

public abstract class SimpleBucketTest<T> {
    @Test
    public void shouldBeEmpty() {
        assertTrue(bucketAdapter().getCollection().isEmpty());
    }

    @Test
    public void shouldAddElement() {
        BucketAdapter<T> adapter = bucketAdapter();
        adapter.add(element());
        assertEquals(1, adapter.getCollection().size());
        assertEquals(element(), adapter.getCollection().stream().findFirst().get());
    }

    @Test
    public void shouldRemoveElement() {
        BucketAdapter<T> adapter = bucketAdapter();
        shouldAddElement();
        adapter.remove(element());
        shouldBeEmpty();
    }

    abstract BucketAdapter<T> bucketAdapter();

    abstract T element();

    public interface BucketAdapter<T> {
        Collection<T> getCollection();

        void add(T element);

        void remove(T element);
    }
}
