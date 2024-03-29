package me.mastercapexd.auth.database.type;

import java.util.List;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.jdbc.db.PostgresDatabaseType;

/**
 * From: <a href="https://github.com/j256/ormlite-core/issues/20#issuecomment-443361114">Source</a>
 */
public class IdentityPostgresDatabaseType extends PostgresDatabaseType {
    @Override
    public boolean isIdSequenceNeeded() {
        return driver.getMajorVersion() < 10;
    }

    @Override
    protected void configureGeneratedId(String tableName, StringBuilder sb, FieldType fieldType,
                                        List<String> statementsBefore, List<String> statementsAfter,
                                        List<String> additionalArgs, List<String> queriesAfter) {
        if (fieldType.isAllowGeneratedIdInsert())
            sb.append("GENERATED BY DEFAULT AS IDENTITY ");
        else
            sb.append("GENERATED ALWAYS AS IDENTITY ");
        configureId(sb, fieldType, statementsBefore, additionalArgs, queriesAfter);
    }
}