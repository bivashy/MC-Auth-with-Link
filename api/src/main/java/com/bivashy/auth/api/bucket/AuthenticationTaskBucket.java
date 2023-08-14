package com.bivashy.auth.api.bucket;

import java.util.List;

import com.bivashy.auth.api.model.AuthenticationTask;

public interface AuthenticationTaskBucket extends Bucket<AuthenticationTask> {
    @Deprecated
    default void addTask(AuthenticationTask task){
        modifiable().add(task);
    }

    @Deprecated
    default void removeTask(AuthenticationTask task){
        modifiable().remove(task);
    }

    @Deprecated
    List<AuthenticationTask> getTasks();
}
