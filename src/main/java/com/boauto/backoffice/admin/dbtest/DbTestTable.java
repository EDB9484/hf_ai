package com.boauto.backoffice.admin.dbtest;

public record DbTestTable(
        String tableSchema,
        String tableName,
        String tableType
) {
}
