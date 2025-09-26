/*
 * Copyright 2023 Flamingock (https://www.flamingock.io)
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

package io.flamingock.examples.couchbase.standalone.changes;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.manager.query.DropQueryIndexOptions;
import io.flamingock.api.annotations.Apply;
import io.flamingock.api.annotations.Change;
import io.flamingock.api.annotations.Rollback;
import io.flamingock.api.annotations.TargetSystem;

import java.util.Collections;

@Change(id = "index-initializer", author = "flamingock-team", transactional = false)
@TargetSystem(id = "couchbase-target-system")
public class _0001__IndexInitializerChange {

    @Apply
    public void apply(Cluster cluster) {
        cluster.queryIndexes().createIndex("bucket", "idx_standalone_index", Collections.singletonList("field1, field2"));
    }

    @Rollback
    public void rollback(Cluster cluster) {
        cluster.queryIndexes().dropIndex("bucket", "idx_standalone_index", DropQueryIndexOptions.dropQueryIndexOptions().ignoreIfNotExists(true));
    }
}
