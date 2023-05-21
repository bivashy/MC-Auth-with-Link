package com.bivashy.auth.api.config.database.schema;

import java.util.Optional;

public interface SchemaSettings {
    Optional<TableSettings> getTableSettings(String key);
}
