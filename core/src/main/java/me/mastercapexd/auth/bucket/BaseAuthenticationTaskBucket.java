package me.mastercapexd.auth.bucket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bivashy.auth.api.bucket.AuthenticationTaskBucket;
import com.bivashy.auth.api.model.AuthenticationTask;

public class BaseAuthenticationTaskBucket implements AuthenticationTaskBucket {
    private final List<AuthenticationTask> tasks = new ArrayList<>();

    @Override
    public void addTask(AuthenticationTask task) {
        tasks.add(task);
    }

    @Override
    public void removeTask(AuthenticationTask task) {
        tasks.remove(task);
    }

    @Override
    public List<AuthenticationTask> getTasks() {
        return Collections.unmodifiableList(tasks);
    }
}
