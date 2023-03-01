package com.bivashy.auth.api.link.command.context;

public interface CustomCommandExecutionContext {
    String getExecutionText();

    boolean isButtonExecution();

    void setButtonExecution(boolean buttonExecution);
}
