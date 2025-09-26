/*
 * Copyright 2025 Flamingock (https://www.flamingock.io)
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
 
package io.flamingock.examples.graalvm.changes;

import com.mongodb.client.MongoDatabase;
import io.flamingock.api.annotations.Apply;
import io.flamingock.api.annotations.Change;
import io.flamingock.api.annotations.Rollback;
import io.flamingock.api.annotations.TargetSystem;

@Change(id = "create-collection", author = "flamingock-team", transactional = false)
@TargetSystem(id ="mongodb-target-system")
public class _0001__CreateCollectionChange {

    @Apply
    public void apply(MongoDatabase mongoDatabase) {
        mongoDatabase.createCollection("clientCollection");
    }

    @Rollback
    public void rollBack(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection("clientCollection").drop();
    }
}
