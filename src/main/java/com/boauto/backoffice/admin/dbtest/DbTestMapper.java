package com.boauto.backoffice.admin.dbtest;

import java.util.List;

public interface DbTestMapper {

    List<DbTestTable> findExistingTables();
}
