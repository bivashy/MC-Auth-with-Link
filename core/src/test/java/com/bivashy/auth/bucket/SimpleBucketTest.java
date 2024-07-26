package com.bivashy.auth.bucket;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import com.bivashy.auth.api.bucket.Bucket;

public abstract class SimpleBucketTest<T> {

    @Test
    public void shouldBeEmpty() {
        assertTrue(getBucket().getUnmodifiableRaw().isEmpty());
    }

    @Test
    public void shouldAddElement() {
        getBucket().modifiable().add(element());
        assertEquals(1, getBucket().getUnmodifiableRaw().size());
        assertEquals(element(), getBucket().stream().findFirst().get());
    }

    @Test
    public void shouldRemoveElement() {
        shouldAddElement();
        getBucket().modifiable().remove(element());
        shouldBeEmpty();
    }

    @Test
    public void shouldAddThenRemove() {
        T element = element();
        getBucket().modifiable().add(element);
        getBucket().modifiable().remove(element);
        shouldBeEmpty();
    }

    abstract Bucket<T> getBucket();

    abstract T element();

}
