package me.mastercapexd.auth.objects;

public class IPInfoResponse {
    private final IPInfoAnswer answer;
    private String countryCode;
    private String townName;
    private String flagEmoji;

    public IPInfoResponse() {
        answer = IPInfoAnswer.ERROR;
    }

    public IPInfoResponse(String countryCode, String townName, String flagEmoji) {
        answer = IPInfoAnswer.SUCCESS;
        this.countryCode = countryCode;
        this.townName = townName;
        this.flagEmoji = flagEmoji;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getTownName() {
        return townName;
    }

    public String getFlagEmoji() {
        return flagEmoji;
    }

    public IPInfoAnswer getAnswer() {
        return answer;
    }

    public String setInfo(String text) {
        if (answer == IPInfoAnswer.ERROR)
            return text.replaceAll("(?i)%country_code%", "NONE").replaceAll("(?i)%town%", "NONE").replaceAll("(?i)%flag_emoji%", "🏳");
        if (answer == IPInfoAnswer.SUCCESS)
            return text.replaceAll("(?i)%country_code%", countryCode).replaceAll("(?i)%town%", townName).replaceAll("(?i)%flag_emoji%", flagEmoji);
        return text;
    }

    public enum IPInfoAnswer {
        SUCCESS, ERROR
    }
}
