package com.bivashy.auth.api.config.database.schema;

import java.util.Optional;

public interface TableSettings {
    String getTableName();

    Optional<String> getColumnName(String key);
}
