package me.mastercapexd.auth.messenger.commands.custom;

public class CustomCommandExecuteContext {
    private final String executionText;
    private boolean isButtonExecution;

    public CustomCommandExecuteContext(String executionText) {
        this.executionText = executionText;
    }

    public String getExecutionText() {
        return executionText;
    }

    public boolean isButtonExecution() {
        return isButtonExecution;
    }

    public CustomCommandExecuteContext setButtonExecution(boolean isButtonExecution) {
        this.isButtonExecution = isButtonExecution;
        return this;
    }
}
