package com.bivashy.auth.api.bucket;

import java.util.List;

import com.bivashy.auth.api.model.AuthenticationTask;

public interface AuthenticationTaskBucket {
    void addTask(AuthenticationTask task);

    void removeTask(AuthenticationTask task);

    List<AuthenticationTask> getTasks();
}
