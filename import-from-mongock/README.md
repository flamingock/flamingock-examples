![Header Image](../misc/logo-with-text.png)
___

# Mongock to Flamingock upgrade guide

## Overview

This guide demonstrates how to upgrade an existing project from Mongock to Flamingock within the same codebase. To illustrate, we have two example states:

- **project-using-mongock**: Your original project configured with Mongock and existing ChangeUnits.
- **project-using-flamingock**: The same project after upgrading dependencies and configuration to Flamingock, now leveraging Flamingock’s enhanced capabilities.

Throughout this guide you’ll update your current project’s ChangeUnits and configuration.

## Table of contents

1. [Upgrade steps](#upgrade-steps)
2. [Before & after states](#before--after-states)
3. [Step 1: Adapt change units](#step-1-adapt-change-units)
4. [Step 2: Update application code](#step-2-update-application-code)
5. [Step 3: Create system stage](#step-3-create-system-stage)
6. [Step 4: Configure pipeline](#step-4-configure-pipeline)
7. [Run and validate](#run-and-validate)
8. [Proven functionalities](#proven-functionalities)

---

## Upgrade steps

Upgrading from Mongock to Flamingock is simple and requires minimal code changes:

1. **[Adapt change units](#step-1-adapt-change-units)** – update imports from `io.mongock` to `io.flamingock`.
2. **[Update application code](#step-2-update-application-code)** – replace the Mongock API calls with the Flamingock builder.
3. **[Create system stage](#step-3-create-system-stage)** – add a template-based ChangeUnit to import legacy audit entries.
4. **[Configure pipeline](#step-4-configure-pipeline)** – define your stages in `resources/flamingock/pipeline.yaml`.

**That’s it!** Your project is now running on Flamingock and ready for advanced features.

---

## Before & after states

### Before (`project-using-mongock`)

Your existing codebase with:

- Mongock dependencies:
  ```groovy
  implementation(platform("io.mongock:mongock-bom:5.5.0"))
  implementation("io.mongock:mongock-standalone")
  implementation("io.mongock:mongodb-sync-v4-driver")
  ```
- ChangeUnits annotated with `io.mongock.api.annotations.*`

### After (`project-using-flamingock`)

The same codebase after upgrading:

- Flamingock dependency, e.g.:
  ```groovy
    annotationProcessor("io.flamingock:flamingock-processor:$flamingockVersion")
    implementation("io.flamingock:flamingock-ce-mongodb-sync:$flamingockVersion")
  ```
- ChangeUnits now import `io.flamingock.api.annotations.*`
- Pipeline configuration in `resources/flamingock/pipeline.yaml`

---

## Step 1: Adapt change units

### Required import changes

| Mongock import                                  | Flamingock import                                   |
|-------------------------------------------------|-----------------------------------------------------|
| `io.mongock.api.annotations.ChangeUnit`         | `io.flamingock.api.annotations.ChangeUnit`          |
| `io.mongock.api.annotations.Execution`          | `io.flamingock.api.annotations.Execution`           |
| `io.mongock.api.annotations.RollbackExecution`  | `io.flamingock.api.annotations.RollbackExecution`   |

### Legacy support

- `@BeforeExecution` and `@RollbackBeforeExecution` from `io.mongock.api` are supported for backward compatibility
- **For legacy change units**: Keep them **exactly as they are** - do not split or modify beyond import changes to maintain immutability
- **For new change units**: Avoid using `@BeforeExecution` and `@RollbackBeforeExecution`. Instead, use dedicated `@Execution` and `@RollbackExecution` methods for better separation of concerns

### Example ChangeUnit

**Before (project-using-mongock):**
```java
package com.yourapp.mongock;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;

@ChangeUnit(id = "client-initializer", order = "1", author = "mongock")
public class ClientInitializer {
  @Execution
  public void execution(MongoDatabase db) {
    db.createCollection("collectionFromMongock");
  }

  @RollbackExecution
  public void rollback() {
    System.out.println("Rollback: client-initializer");
  }
}
```

**After (project-using-flamingock):**
- Just changing the annotation packages

```java
package com.yourapp.mongock;

import io.flamingock.api.annotations.ChangeUnit;
import io.flamingock.api.annotations.Execution;
import io.flamingock.api.annotations.RollbackExecution;

@ChangeUnit(id = "client-initializer", order = "1", author = "mongock")
public class ClientInitializer {
  @Execution
  public void execution(MongoDatabase db) {
    db.createCollection("collectionFromMongock");
  }

  @RollbackExecution
  public void rollback() {
    System.out.println("Rollback: client-initializer");
  }
}
```

---

## Step 2: Update application code

Replace your Mongock API usage with Flamingock SDK in your main application class:

**Original Mongock application:**
```java
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.mongock.driver.mongodb.sync.v4.driver.MongoSync4Driver;
import io.mongock.runner.standalone.MongockStandalone;

public static void main(String[] args) {
    MongoClientSettings build = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString("mongodb://localhost:27017/")).build();

    try(MongoClient mongoClient = MongoClients.create(build)) {
        MongockStandalone.builder()
                .setDriver(MongoSync4Driver.withDefaultLock(mongoClient, "test"))
                .addMigrationScanPackage("legacy.mongock.changes")
                .buildRunner()
                .execute();
    }
}
```

**Updated Flamingock application:**
```java
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.flamingock.community.Flamingock;

public class FlamingockApplication {

    public static void main(String[] args) {
        MongoClientSettings build = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("mongodb://localhost:27017/")).build();

        try (MongoClient mongoClient = MongoClients.create(build)) {
            Flamingock.builder()
                    .addDependency(mongoClient)
                    .addDependency(mongoClient.getDatabase("test"))
                    .build()
                    .run();
        }
    }
}
```

**Key changes:**
- Replace `MongockStandalone` with `Flamingock.builder()`
- Remove driver configuration - Flamingock handles this automatically
- Remove package scanning - Flamingock uses pipeline configuration instead
- Add dependencies directly using `.addDependency()`

**Advanced integrations:**
For [Spring Boot]((https://docs.flamingock.io/docs/springboot-integration/introduction)) and other advanced integrations, visit [Flamingock's official documentation](https://docs.flamingock.io).

## Step 3: Create system stage

Create a package where the flamingock system changeUnits will be placed(for now only the import changeUnit).
This package is the one you will specify in the pipeline descriptor. In this case we'll use `com.yourapp.flamingock.system`
```yaml
id: migration-from-mongock
order: 0001
template: MongoDbImporterChangeTemplate
configuration:
  origin: mongockChangeLog
```

**Configuration parameters:**
- **id**: Choose how you want to identify this change unit
- **order**: Should be the first one (0001) as this is typically the first system stage change unit
- **template**: Must be `MongoDbImporterChangeTemplate`
- **origin**: The collection/table where Mongock's audit log is stored (typically `mongockChangeLog`)

## Step 4: Configure pipeline

Configure the Flamingock pipeline using the `@Pipeline` annotation on any class in your project:

```java
@Pipeline(
  systemStage = @SystemStage(sourcesPackage = "com.yourapp.flamingock.system"),
  stages = {
    @Stage(name = "legacy-stage", type = LEGACY, sourcesPackage = "com.yourapp.mongock"),
    @Stage(name = "New MongoDB changes", sourcesPackage = "com.yourapp.flamingock.mongodb")
  }
)
public class AnyClassInYourProject {
}
```

### Key configuration elements:
- Add this annotation to any class (configuration class, main class, or test class)
- The **System Stage** contains framework-provided ChangeUnits—like the importer that reads Mongock’s legacy changelog and replays it into Flamingock’s audit store.
- The **Legacy Stage** hosts your existing ChangeUnits(initially created with Mongock) with only the import paths updated, preserving their original logic. Flamingock will execute each legacy ChangeUnit only if it hasn't already been applied under Mongock, preventing duplicate runs.
- The **Regular Stages** Contains your new Flamingock change units. In this example we’ve dedicated a stage to MongoDB changes, but you could similarly add stages for Kafka, S3, or bundle multiple systems into a single stage—choose the organization that best fits your project

## Run and validate

### Running the migration
After you’ve updated your project and pipeline, run Flamingock as usual:

```shell
./gradlew run
```
#### Execution flow

- **System stage**: Flamingock first executes the system ChangeUnit (migration-from-mongock) to import any outstanding Mongoock audit entries into the Flamingock store.

- **Legacy stage**: Next, Flamingock runs each existing ChangeUnits(initially created with Mongock).
  - Since these ChangeUnits have already been applied under Mongock, none will re-execute.
  - If for any reason a legacy ChangeUnit was never applied, Flamingock will pick it up and execute it now—ensuring nothing is missed.

- **Regular stages**: Finally, Flamingock applies your new, native ChangeUnits—in this example, the MongoDB-specific changes.


### Expected output

After running Flamingock, you should see output similar to:
```
Stage: flamingock-system-stage
	0001) id: migration-from-mongock
		Started				✅ - OK
		Executed			✅ - OK
		Audited[execution]	        ✅ - OK

Stage: New MongoDB changes
	0001) id: create-users-collection-with-index
		Started				✅ - OK
		Executed			✅ - OK
		Audited[execution]	        ✅ - OK
	0002) id: seed-users
		Started				✅ - OK
		Executed			✅ - OK
		Audited[execution]	        ✅ - OK
```

### Validation checklist

- ✅ System stage executes the migration change unit successfully
- ✅ Legacy change units from Mongock execute without errors
- ✅ New Flamingock change units execute as expected
- ✅ All audit logs are properly created in Flamingock format
- ✅ Database changes match the expected results

### Example comparison

To validate the migration worked correctly, you can compare with the original Mongock project:

```shell
# Run original Mongock project
cd mongock-legacy
./gradlew run

# Run migrated Flamingock project
cd ../flamingock-imported
./gradlew run
```

Both should produce the same database changes, demonstrating successful migration.

## Proven functionalities

This example demonstrates:

1. **Seamless Migration**: Existing Mongock change units work in Flamingock with minimal changes
2**Pipeline Configuration**: Proper setup for migrated and new change units
3**System Stage Integration**: Automatic upgrade of Mongock change logs to Flamingock audit logs

---

### Additional resources

For detailed migration instructions, visit our [technical documentation on Mongock migration](https://docs.flamingock.io/migration/mongock).

---

### Contributing
We welcome contributions! If you have an idea for a new example or improvement to an existing one, feel free to submit a
pull request. Check out our [CONTRIBUTING.md](../CONTRIBUTING.md) for guidelines.

---

### Get involved
Star the [Flamingock repository](https://github.com/mongock/flamingock-project) to show your support!

Report issues or suggest features in the [Flamingock issue tracker](https://github.com/mongock/flamingock-project/issues).

Join the discussion in the [Flamingock community](https://github.com/mongock/flamingock-project/discussions).

---

### License
This repository is licensed under the [Apache License 2.0](../LICENSE.md).

---

### Explore, experiment, and empower your projects with Flamingock!
Let us know what you think or where you'd like to see Flamingock used next. your projects with Flamingock!
Let us know what you think or where you'd like to see Flamingock used next.
