/*
 * Copyright 2023 Flamingock (https://oss.flamingock.io)
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

import io.flamingock.api.annotations.ChangeUnit;
import io.flamingock.api.annotations.Execution;
import io.flamingock.examples.mongodb.springboot.springdata.client.Client;
import io.flamingock.examples.mongodb.springboot.springdata.client.ClientRepository;

@ChangeUnit( id="insert-another-document" , order = "0003")
public class CInsertAnotherDocument {

    @Execution
    public void execution(ClientRepository clientRepository) {
        clientRepository.save(new Client("Jorge", null, null, null));
    }
}
