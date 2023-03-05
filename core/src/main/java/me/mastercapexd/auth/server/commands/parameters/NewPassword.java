package me.mastercapexd.auth.server.commands.parameters;

public class NewPassword {
    private final String newPassword;

    public NewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
