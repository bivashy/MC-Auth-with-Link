package me.mastercapexd.auth.bucket;

import com.bivashy.auth.api.bucket.LinkAuthenticationBucket;
import com.bivashy.auth.api.link.user.LinkUser;

public class BaseLinkAuthenticationBucket<T extends LinkUser> extends BaseListBucket<T> implements LinkAuthenticationBucket<T> {

}
