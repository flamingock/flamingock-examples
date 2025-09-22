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

package io.flamingock.examples.mongodb.springboot.springdata.changes;

import io.flamingock.api.annotations.Apply;
import io.flamingock.api.annotations.Change;
import io.flamingock.api.annotations.TargetSystem;
import io.flamingock.examples.mongodb.springboot.springdata.client.Client;
import io.flamingock.examples.mongodb.springboot.springdata.client.ClientRepository;

@Change( id="insert-document" , order = "0002", author = "flamingock-team")
@TargetSystem(id = "mongodb-springdata-target-system")
public class _0002_InsertDocumentChange {

    @Apply
    public void apply(ClientRepository clientRepository) {
        clientRepository.save(new Client("Federico", null, null, null));
    }
}
