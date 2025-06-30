package com.yourapp.mongock;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;

@ChangeUnit(id = "client-updater", order = "2", author = "mongock")
public class ClientUpdater {

    @Execution
    public void execution() {
        System.out.println("Execution: client-updater");
    }

    @RollbackExecution
    public void rollback() {
        System.out.println("Rollback: client-updater");
    }
}
