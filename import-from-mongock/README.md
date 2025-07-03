![Header Image](../misc/logo-with-text.png)
___

# Mongock to Flamingock Migration Example

## Example overview

Welcome to the Mongock to Flamingock Migration Example. This demonstrates how to migrate an existing Mongock project to Flamingock, preserving your existing change units while leveraging Flamingock's enhanced capabilities.

This example contains two projects:
- **mongock-legacy**: The original project using Mongock with existing change units
- **flamingock-imported**: The migrated project using Flamingock with the same change units plus new template-based change units

The migration process involves copying your existing Mongock change units to the new Flamingock project and updating only the package imports, keeping the implementation **exactly the same**. Additionally, new Flamingock-native change units can be added to leverage enhanced capabilities.

## Table of contents

1. [Migration steps](#migration-steps)
2. [Migration overview](#migration-overview)
3. [Step 1: Adapt change units](#step-1-adapt-change-units)
4. [Step 2: Update application code](#step-2-update-application-code)
5. [Step 3: Create system stage](#step-3-create-system-stage)
6. [Step 4: Configure pipeline](#step-4-configure-pipeline)
7. [Run and validate](#run-and-validate)
8. [Proven functionalities](#-proven-functionalities)

---

## Migration steps

Migrating from Mongock to Flamingock is straightforward and requires minimal changes:

1. **[Adapt change units](#step-1-adapt-change-units)** - Update your existing change units by replacing Mongock package imports with Flamingock equivalents (only 3 simple import changes)
2. **[Update application code](#step-2-update-application-code)** - Replace Mongock API with Flamingock SDK in your main application
3. **[Create system stage](#step-3-create-system-stage)** - Add one YAML file to handle the migration of audit logs
4. **[Configure pipeline](#step-4-configure-pipeline)** - Set up the pipeline.yaml with legacy and new stages

**That's it!** Your migration is complete and you can start leveraging Flamingock's advanced features.

---

## Migration overview

This example demonstrates migrating from Mongock to Flamingock with:

### Original Mongock project (`mongock-legacy`)
- **Package**: `legacy.mongock.changes`
- **Change Units**: 
  - `ClientInitializer` (order: 1)
  - `ClientUpdater` (order: 2)

### Migrated Flamingock project (`flamingock-imported`)
- **Legacy Package**: `io.flamingock.examples.importer.flamingock.legacy` (migrated change units)
- **New Package**: `io.flamingock.examples.importer.flamingock.mongodb` (new template-based change units)
- **Same Change Units**: Copied with updated imports only
- **New Change Units**: Template-based change units for MongoDB (could be for any technology like S3, Kafka, etc.)
- **Pipeline Configuration**: `resources/flamingock/pipeline.yaml`

## Step 1: Adapt change units

### Required package import changes

When migrating change units from Mongock to Flamingock, update only these imports:

| Mongock Import                                 | Flamingock Import                                 |
|------------------------------------------------|---------------------------------------------------|
| `io.mongock.api.annotations.ChangeUnit`        | `io.flamingock.api.annotations.ChangeUnit`        |
| `io.mongock.api.annotations.Execution`         | `io.flamingock.api.annotations.Execution`         |
| `io.mongock.api.annotations.RollbackExecution` | `io.flamingock.api.annotations.RollbackExecution` |

### Legacy support

- `@BeforeExecution` and `@RollbackBeforeExecution` from `io.mongock.api` are supported for backward compatibility
- **For migrated change units**: Keep them **exactly as they are** - do not split or modify beyond import changes to maintain immutability
- **For new change units**: Avoid using `@BeforeExecution` and `@RollbackBeforeExecution`. Instead, use dedicated `@Execution` and `@RollbackExecution` methods for better separation of concerns

### Example migration

**Original Mongock change unit:**
```java
package legacy.mongock.changes;

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

**Migrated Flamingock change unit:**
```java
package io.flamingock.examples.importer.flamingock.legacy;

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

## Step 2: Update application code

Replace your Mongock API usage with Flamingock SDK in your main application class:

**Original Mongock application:**
```java
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

**Migrated Flamingock application:**
```java
package io.flamingock.examples.importer;

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
                    .setProperty("mongodb.databaseName", "test")
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

You must create a template-based change unit in the system stage package to handle the migration from Mongock. Create a YAML file (e.g., `_0001_migration_from_mongock.yaml`) with the following structure:

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

The Flamingock pipeline configuration (`resources/flamingock/pipeline.yaml`) requires two key stages:

```yaml
pipeline:
  systemStage:
    sourcesPackage: "io.flamingock.examples.importer.flamingock.system"
  stages:
    - name: "Legacy changes from Mongock"
      type: "legacy"
      sourcesPackage: "io.flamingock.examples.importer.flamingock.legacy"
    - name: "New MongoDB changes"
      description: "Changes to MongoDB"
      sourcesPackage: "io.flamingock.examples.importer.flamingock.mongodb"
```

### Key configuration elements:

1. **System Stage**: Contains the migration change unit that imports Mongock change logs and transforms them to Flamingock audit logs
2. **Legacy Stage**: Contains your migrated change units from Mongock (type: "legacy")
3. **Regular Stages**: For new Flamingock-native change units

## Run and validate

### Running the migration

```shell
./gradlew run
```

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

## ✅ Proven functionalities

This example demonstrates:

1. **Seamless Migration**: Existing Mongock change units work in Flamingock with minimal changes
2. **Backward Compatibility**: Legacy Mongock annotations are supported
3. **Pipeline Configuration**: Proper setup for migrated and new change units
4. **System Stage Integration**: Automatic migration of Mongock change logs to Flamingock audit logs

---

### Additional resources

For detailed migration instructions, visit our [technical documentation on Mongock migration](https://docs.flamingock.io/migration/mongock).

---

### 📢 Contributing
We welcome contributions! If you have an idea for a new example or improvement to an existing one, feel free to submit a
pull request. Check out our [CONTRIBUTING.md](../CONTRIBUTING.md) for guidelines.

___

### 🤝 Get involved
⭐ Star the [Flamingock repository](https://github.com/mongock/flamingock-project) to show your support!

🐞 Report issues or suggest features in the [Flamingock issue tracker](https://github.com/mongock/flamingock-project/issues).

💬 Join the discussion in the [Flamingock community](https://github.com/mongock/flamingock-project/discussions).

___

### 📜 License
This repository is licensed under the [Apache License 2.0](../LICENSE.md).

___

### 🔥 Explore, experiment, and empower your projects with Flamingock!
Let us know what you think or where you’d like to see Flamingock used next.