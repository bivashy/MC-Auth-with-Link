package me.mastercapexd.auth.config.discord;

import java.util.ArrayList;
import java.util.List;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.annotation.ImportantField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

public class RoleModificationSettings implements ConfigurationHolder {
    @ConfigField("type")
    private Type type;
    @ConfigField("role-id")
    @ImportantField
    private long roleId;
    @ConfigField("have-permission")
    private List<String> havePermission = new ArrayList<>();
    @ConfigField("absent-permission")
    private List<String> absentPermission = new ArrayList<>();

    public RoleModificationSettings(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    public Type getType() {
        return type;
    }

    public long getRoleId() {
        return roleId;
    }

    public List<String> getHavePermission() {
        return havePermission;
    }

    public List<String> getAbsentPermission() {
        return absentPermission;
    }

    public enum Type {
        GIVE_ROLE, REMOVE_ROLE
    }
}
