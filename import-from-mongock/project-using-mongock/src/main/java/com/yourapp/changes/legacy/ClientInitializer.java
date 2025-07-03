package com.yourapp.changes.legacy;


import com.mongodb.client.MongoDatabase;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;

@ChangeUnit(id = "client-initializer", order = "1", author = "mongock")
public class ClientInitializer {

    @BeforeExecution
    public void before() {
        System.out.println("Before: client-initializer");
    }

    @RollbackBeforeExecution
    public void beforeRollback() {
        System.out.println("Before-rollback: client-initializer");
    }

    @Execution
    public void execution(MongoDatabase db) {
        db.createCollection("collectionFromMongock");
        System.out.println("Execution: client-initializer");
    }

    @RollbackExecution
    public void rollback() {
        System.out.println("Rollback: client-initializer");
    }
}
