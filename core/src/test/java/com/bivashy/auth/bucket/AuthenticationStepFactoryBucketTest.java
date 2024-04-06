package com.bivashy.auth.bucket;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bivashy.auth.api.bucket.AuthenticationStepFactoryBucket;
import com.bivashy.auth.api.bucket.Bucket;
import com.bivashy.auth.api.factory.AuthenticationStepFactory;

import me.mastercapexd.auth.bucket.BaseAuthenticationStepFactoryBucket;

@ExtendWith(MockitoExtension.class)
public class AuthenticationStepFactoryBucketTest extends SimpleBucketTest<AuthenticationStepFactory> {

    @Mock
    private AuthenticationStepFactory stepFactory;
    private AuthenticationStepFactoryBucket bucket;

    @BeforeEach
    public void setup() {
        bucket = new BaseAuthenticationStepFactoryBucket();
    }

    @Override
    Bucket<AuthenticationStepFactory> getBucket() {
        return bucket;
    }

    @Override
    AuthenticationStepFactory element() {
        return stepFactory;
    }

}
