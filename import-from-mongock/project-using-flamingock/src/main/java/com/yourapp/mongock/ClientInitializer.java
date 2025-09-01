/*
 * Copyright 2025 Flamingock (https://oss.flamingock.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package com.yourapp.mongock;


//import io.mongock.api.annotations.ChangeUnit;
//import io.mongock.api.annotations.Execution;
//import io.flamingock.api.annotations.RollbackExecution;

import com.mongodb.client.MongoDatabase;
import io.flamingock.api.annotations.ChangeUnit;
import io.flamingock.api.annotations.Execution;
import io.flamingock.api.annotations.RollbackExecution;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.RollbackBeforeExecution;

import java.util.ArrayList;


/**
 * Migration example demonstrating Flamingock's native annotations.
 *
 * <p>This change unit represents {@code io.mongock.changes.ClientInitializer} migrated to Flamingock's API.
 * The implementation should be <strong>exactly the same</strong> as the original Mongock version, with only
 * package imports updated as follows:
 * <ul>
 * <li>Replace {@code io.mongock.api.annotations.ChangeUnit} with {@code io.flamingock.api.annotations.ChangeUnit}</li>
 * <li>Replace {@code io.mongock.api.annotations.Execution} with {@code io.flamingock.api.annotations.Execution}</li>
 * <li>Replace {@code io.mongock.api.annotations.RollbackExecution} with {@code io.flamingock.api.annotations.RollbackExecution}</li>
 * </ul>
 * </p>
 *
 * <p><strong>Legacy Support:</strong>
 * The {@code @BeforeExecution} {@code @RollbackBeforeExecution} annotations (from {@code io.mongock.api}) are supported
 * for backward compatibility but is <strong>strongly discouraged</strong>. Instead, split your logic into separate change
 * units with dedicated {@code @Execution} and {@code @RollbackExecution} methods for better separation of concerns,
 * improved testability, and clearer rollback semantics and full Flamingock support.</p>
 */
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
        boolean isAlreadyCreatedCollection = db.listCollectionNames()
                .into(new ArrayList<>())
                .contains("collectionFromMongock");
        if(isAlreadyCreatedCollection) {
            throw new RuntimeException("Collection already created");
        }
        db.createCollection("collectionFromMongock");
        System.out.println("Execution: client-initializer");
    }

    @RollbackExecution
    public void rollback() {
        System.out.println("Rollback: client-initializer");
    }
}
