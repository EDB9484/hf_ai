package com.boauto.backoffice.admin.dbtest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DbTestController {

    private final DbTestService dbTestService;

    public DbTestController(DbTestService dbTestService) {
        this.dbTestService = dbTestService;
    }

    @GetMapping("/db-test")
    public List<DbTestTable> getDbTest() {
        return dbTestService.getExistingTables();
    }
}
