package me.mastercapexd.auth.bucket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import com.bivashy.auth.api.bucket.Bucket;
import com.bivashy.auth.api.bucket.ModifiableBucket;

public class BaseListBucket<T> implements Bucket<T> {
    private final List<T> list;
    private final ModifiableBucket<T> modifiableBucket;

    public BaseListBucket(List<T> list, ModifiableBucket<T> modifiableBucket) {
        this.list = list;
        this.modifiableBucket = modifiableBucket;
    }

    public BaseListBucket(List<T> list) {
        this(list, new BaseModifiableListBucket<>(list));
    }

    public BaseListBucket(){
        this(new ArrayList<>());
    }

    @Override
    public Collection<T> getUnmodifiableRaw() {
        return Collections.unmodifiableList(list);
    }

    @Override
    public Stream<T> stream() {
        return list.stream();
    }

    @Override
    public ModifiableBucket<T> modifiable() {
        return modifiableBucket;
    }
}
