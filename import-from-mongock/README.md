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

1. [Migration overview](#migration-overview)
2. [Change unit migration](#change-unit-migration)
3. [Pipeline configuration](#pipeline-configuration)
4. [How to run this example](#how-to-run-this-example)
5. [Proven functionalities](#proven-functionalities)

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

## Change unit migration

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

## Pipeline configuration

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

## How to run this example

### 1. Run Mongock legacy project
```shell
cd mongock-legacy
./gradlew test
```

### 2. Run Flamingock imported project
```shell
cd flamingock-imported
./gradlew test
```

### 3. Compare results
Both projects should produce the same database changes, demonstrating successful migration.

After running the Flamingock project, you should see output similar to:
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

## Proven functionalities

This example demonstrates:

1. **Seamless Migration**: Existing Mongock change units work in Flamingock with minimal changes
2. **Backward Compatibility**: Legacy Mongock annotations are supported
3. **Pipeline Configuration**: Proper setup for migrated and new change units
4. **System Stage Integration**: Automatic migration of Mongock change logs to Flamingock audit logs

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
Let us know what you think or where you'd like to see Flamingock used next.