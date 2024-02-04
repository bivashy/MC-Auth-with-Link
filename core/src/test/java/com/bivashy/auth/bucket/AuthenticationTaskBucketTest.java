package com.bivashy.auth.bucket;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bivashy.auth.api.bucket.AuthenticationTaskBucket;
import com.bivashy.auth.api.bucket.Bucket;
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
    Bucket<AuthenticationTask> getBucket() {
        return bucket;
    }

    @Override
    AuthenticationTask element() {
        return task;
    }

}
