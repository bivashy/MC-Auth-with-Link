package com.bivashy.auth.bucket;

import java.util.Collection;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bivashy.auth.api.bucket.AuthenticationTaskBucket;
import com.bivashy.auth.api.model.AuthenticationTask;

import me.mastercapexd.auth.bucket.BaseAuthenticationTaskBucket;

@ExtendWith(MockitoExtension.class)
public class AuthenticationTaskBucketTest extends SimpleBucketTest<AuthenticationTask> {
    private AuthenticationTaskBucket bucket;
    @Mock
    private AuthenticationTask task;

    @BeforeEach
    public void setup() {
        bucket = new BaseAuthenticationTaskBucket();
    }

    @Override
    BucketAdapter<AuthenticationTask> bucketAdapter() {
        return new BucketAdapter<AuthenticationTask>() {
            @Override
            public Collection<AuthenticationTask> getCollection() {
                return bucket.getTasks();
            }

            @Override
            public void add(AuthenticationTask element) {
                bucket.addTask(element);
            }

            @Override
            public void remove(AuthenticationTask element) {
                bucket.removeTask(element);
            }
        };
    }

    @Override
    AuthenticationTask element() {
        return task;
    }
}
