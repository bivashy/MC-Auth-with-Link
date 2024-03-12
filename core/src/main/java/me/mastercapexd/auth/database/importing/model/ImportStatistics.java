package me.mastercapexd.auth.database.importing.model;

import java.util.Objects;

public class ImportStatistics {

    private int accounts;
    private int linkAccounts;
    private int invalidEntries;
    private boolean failed;

    public ImportStatistics() {
    }

    public ImportStatistics(int accounts, int linkAccounts, int invalidEntries, boolean failed) {
        this.accounts = accounts;
        this.linkAccounts = linkAccounts;
        this.invalidEntries = invalidEntries;
        this.failed = failed;
    }

    public boolean success() {
        return !failed;
    }

    public void fail() {
        failed = true;
    }

    public void accountAdded() {
        accounts++;
    }

    public void linkAccountAdded() {
        linkAccounts++;
    }

    public void invalidEntrySkipped() {
        invalidEntries++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ImportStatistics that = (ImportStatistics) o;
        return accounts == that.accounts && linkAccounts == that.linkAccounts && invalidEntries == that.invalidEntries && failed == that.failed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accounts, linkAccounts, invalidEntries, failed);
    }

    @Override
    public String toString() {
        String state = success() ? "(SUCCESS)" : "(FAILED)";
        return "-- Import statistics " + state + " --" + "\n" +
                "Account: " + accounts + "\n" +
                "Account links: " + linkAccounts + "\n" +
                "Invalid entries: " + invalidEntries;
    }

}
