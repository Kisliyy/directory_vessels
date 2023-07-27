package com.smartgeosystems.vessels_core.config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;

public class LiquibaseMigration {

    public static void initMigration(Connection connection) throws DatabaseException {
        Database database = DatabaseFactory
                .getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase =
                new Liquibase(
                        "classpath:db/liquibase/master.xml",
                        new ClassLoaderResourceAccessor(),
                        database
                );
    }
}
