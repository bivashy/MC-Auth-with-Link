package me.mastercapexd.auth.proxy.commands.parameters;

public class DoublePassword {
    private final String oldPassword, newPassword;

    public DoublePassword(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

}
