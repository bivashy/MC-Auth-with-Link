package com.bivashy.auth.bucket;

import java.util.Collection;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bivashy.auth.api.bucket.LinkConfirmationBucket;
import com.bivashy.auth.api.link.user.confirmation.LinkConfirmationUser;

import me.mastercapexd.auth.bucket.BaseLinkConfirmationBucket;

@ExtendWith(MockitoExtension.class)
public class LinkConfirmationBucketTest extends SimpleBucketTest<LinkConfirmationUser> {
    private LinkConfirmationBucket bucket;
    @Mock
    private LinkConfirmationUser user;

    @BeforeEach
    public void setup() {
        bucket = new BaseLinkConfirmationBucket();
    }

    @Override
    BucketAdapter<LinkConfirmationUser> bucketAdapter() {
        return new BucketAdapter<LinkConfirmationUser>() {
            @Override
            public Collection<LinkConfirmationUser> getCollection() {
                return bucket.getConfirmationUsers();
            }

            @Override
            public void add(LinkConfirmationUser element) {
                bucket.addLinkConfirmation(element);
            }

            @Override
            public void remove(LinkConfirmationUser element) {
                bucket.removeLinkConfirmation(element);
            }
        };
    }

    @Override
    LinkConfirmationUser element() {
        return user;
    }
}
