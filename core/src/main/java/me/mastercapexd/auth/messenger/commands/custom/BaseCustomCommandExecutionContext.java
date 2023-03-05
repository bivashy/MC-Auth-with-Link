package me.mastercapexd.auth.messenger.commands.custom;

import com.bivashy.auth.api.link.command.context.CustomCommandExecutionContext;

public class BaseCustomCommandExecutionContext implements CustomCommandExecutionContext {
    private final String executionText;
    private boolean isButtonExecution;

    public BaseCustomCommandExecutionContext(String executionText) {
        this.executionText = executionText;
    }

    public String getExecutionText() {
        return executionText;
    }

    public boolean isButtonExecution() {
        return isButtonExecution;
    }

    @Override
    public void setButtonExecution(boolean isButtonExecution) {
        this.isButtonExecution = isButtonExecution;
    }
}
