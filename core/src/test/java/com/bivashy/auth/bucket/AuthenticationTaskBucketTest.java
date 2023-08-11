package com.bivashy.auth.bucket;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bivashy.auth.api.bucket.AuthenticationTaskBucket;
import com.bivashy.auth.api.model.AuthenticationTask;

import me.mastercapexd.auth.bucket.BaseAuthenticationTaskBucket;

@ExtendWith(MockitoExtension.class)
public class AuthenticationTaskBucketTest {
    private final List<AuthenticationTask> tasks = new ArrayList<>();
    private AuthenticationTaskBucket bucket;
    @Mock
    private AuthenticationTask task;

    @BeforeEach
    public void setup() {
        bucket = new BaseAuthenticationTaskBucket();
    }

    @Test
    public void shouldBeEmpty() {
        assertTrue(bucket.getTasks().isEmpty());
    }

    @Test
    public void shouldAddTask() {
        bucket.addTask(task);
        assertEquals(1, bucket.getTasks().size());
        assertEquals(task, bucket.getTasks().get(0));
    }

    @Test
    public void shouldRemoveTask() {
        shouldAddTask();
        bucket.removeTask(task);
        shouldBeEmpty();
    }
}
