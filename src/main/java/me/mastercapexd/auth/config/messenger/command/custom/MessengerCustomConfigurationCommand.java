package me.mastercapexd.auth.config.messenger.command.custom;

import java.util.regex.Pattern;

import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.messenger.commands.custom.CustomCommandExecuteContext;
import me.mastercapexd.auth.messenger.commands.custom.MessengerCustomCommand;

public class MessengerCustomConfigurationCommand implements MessengerCustomCommand, ConfigurationHolder {

    private final ConfigurationSectionHolder sectionHolder;
    private final String key;
    private final String answer;
    private Pattern matchCommand = null;
    private boolean ignoreButtons = false;

    public MessengerCustomConfigurationCommand(String key, ConfigurationSectionHolder sectionHolder) {
        this.key = key;
        this.sectionHolder = sectionHolder;
        answer = sectionHolder.getString("answer");
        if (sectionHolder.contains("regex"))
            matchCommand = Pattern.compile(sectionHolder.getString("regex"));
        if (sectionHolder.contains("button-ignore"))
            ignoreButtons = sectionHolder.getBoolean("button-ignore");
    }

    @Override
    public boolean shouldExecute(CustomCommandExecuteContext context) {
        if (ignoreButtons && context.isButtonExecution())
            return false;
        if (context.getExecutionText() == null)
            return false;
        if (matchCommand == null) {
            return key.equalsIgnoreCase(context.getExecutionText());
        } else {
            return matchCommand.matcher(context.getExecutionText()).matches();
        }
    }

    @Override
    public String getAnswer() {
        return answer;
    }

    @Override
    public ConfigurationSectionHolder getSectionHolder() {
        return sectionHolder;
    }
}