package com.boauto.backoffice.admin.dbtest;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbTestService {

    private final DbTestMapper dbTestMapper;

    public DbTestService(DbTestMapper dbTestMapper) {
        this.dbTestMapper = dbTestMapper;
    }

    public List<DbTestTable> getExistingTables() {
        return dbTestMapper.findExistingTables();
    }
}
