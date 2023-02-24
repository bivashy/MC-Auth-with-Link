package me.mastercapexd.auth.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuthenticationTaskBucket {
    private final List<AuthenticationTask> tasks = new ArrayList<>();

    public void addTask(AuthenticationTask task) {
        tasks.add(task);
    }

    public void removeTask(AuthenticationTask task) {
        tasks.remove(task);
    }

    public List<AuthenticationTask> getTasks() {
        return Collections.unmodifiableList(tasks);
    }
}
