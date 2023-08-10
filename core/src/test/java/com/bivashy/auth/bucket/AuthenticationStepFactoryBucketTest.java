package com.bivashy.auth.bucket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Iterator;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bivashy.auth.api.bucket.AuthenticationStepFactoryBucket;
import com.bivashy.auth.api.factory.AuthenticationStepFactory;

import me.mastercapexd.auth.bucket.BaseAuthenticationStepFactoryBucket;

@ExtendWith(MockitoExtension.class)
public class AuthenticationStepFactoryBucketTest {
    private static final String WRONG_STEP_NAME = "WRONG";
    private static final String STEP_NAME = "TEST";
    @Mock
    private AuthenticationStepFactory stepFactory;
    private AuthenticationStepFactoryBucket bucket;

    @BeforeEach
    public void setup() {
        bucket = new BaseAuthenticationStepFactoryBucket();
    }

    @Test
    public void shouldBeEmpty() {
        assertEquals(0, bucket.getList().size());
        assertFalse(bucket.findFirst(stepFactory -> stepFactory.getAuthenticationStepName().equals(STEP_NAME)).isPresent());

        Iterator<AuthenticationStepFactory> factoryIterator = bucket.iterator();
        assertFalse(factoryIterator.hasNext());
    }

    @Test
    public void shouldAddStepAndNotFindWrongStep() {
        when(stepFactory.getAuthenticationStepName()).thenReturn(STEP_NAME);

        bucket.add(stepFactory);

        assertTrue(bucket.findFirst(stepFactory -> stepFactory.getAuthenticationStepName().equals(STEP_NAME)).isPresent());
        assertEquals(1, bucket.getList().size());
        assertEquals(stepFactory, bucket.getList().get(0));
        assertEquals(stepFactory, bucket.iterator().next());

        assertFalse(bucket.findFirst(stepFactory -> stepFactory.getAuthenticationStepName().equals(WRONG_STEP_NAME)).isPresent());
        assertNotEquals(WRONG_STEP_NAME, bucket.getList().get(0).getAuthenticationStepName());
        assertNotEquals(WRONG_STEP_NAME, bucket.iterator().next().getAuthenticationStepName());
    }

    @Test
    public void shouldAddThenRemoveStep() {
        bucket.add(stepFactory);
        bucket.remove(stepFactory);

        shouldBeEmpty();
    }
}
