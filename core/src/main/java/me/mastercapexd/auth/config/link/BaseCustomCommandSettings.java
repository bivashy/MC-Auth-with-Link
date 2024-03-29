package me.mastercapexd.auth.config.link;

import java.util.regex.Pattern;

import com.bivashy.auth.api.config.link.command.LinkCustomCommandSettings;
import com.bivashy.auth.api.link.command.context.CustomCommandExecutionContext;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

public class BaseCustomCommandSettings implements LinkCustomCommandSettings, ConfigurationHolder {
    private final ConfigurationSectionHolder sectionHolder;
    private final String key;
    private final String answer;
    private Pattern matchCommand = null;
    private boolean ignoreButtons = false;

    public BaseCustomCommandSettings(String key, ConfigurationSectionHolder sectionHolder) {
        this.key = key;
        this.sectionHolder = sectionHolder;
        answer = sectionHolder.getString("answer");
        if (sectionHolder.contains("regex"))
            matchCommand = Pattern.compile(sectionHolder.getString("regex"));
        if (sectionHolder.contains("button-ignore"))
            ignoreButtons = sectionHolder.getBoolean("button-ignore");
    }

    @Override
    public boolean shouldExecute(CustomCommandExecutionContext context) {
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