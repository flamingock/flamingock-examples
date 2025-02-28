
rootProject.name = "flamingock-examples"

/**
 * MONGODB
 */
include("examples:mongodb:mongodb-springboot-springdata")
project(":examples:mongodb:mongodb-springboot-springdata").projectDir = file("examples/mongodb/mongodb-springboot-springdata")
project(":examples:mongodb:mongodb-springboot-springdata").name = "mongodb-springboot-springdata"

include("examples:mongodb:mongodb-springboot-sync")
project(":examples:mongodb:mongodb-springboot-sync").projectDir = file("examples/mongodb/mongodb-springboot-sync")
project(":examples:mongodb:mongodb-springboot-sync").name = "mongodb-springboot-sync"

include("examples:mongodb:mongodb-springboot-v3-springdata-v4")
project(":examples:mongodb:mongodb-springboot-v3-springdata-v4").projectDir = file("examples/mongodb/mongodb-springboot-v3-springdata-v4")
project(":examples:mongodb:mongodb-springboot-v3-springdata-v4").name = "mongodb-springboot-v3-springdata-v4"

include("examples:mongodb:mongodb-sync-standalone")
project(":examples:mongodb:mongodb-sync-standalone").projectDir = file("examples/mongodb/mongodb-sync-standalone")
project(":examples:mongodb:mongodb-sync-standalone").name = "mongodb-sync-standalone"

include("examples:mongodb:mongodb-sync-standalone")
project(":examples:mongodb:mongodb-sync-standalone").projectDir = file("examples/mongodb/mongodb-sync-standalone")
project(":examples:mongodb:mongodb-sync-standalone").name = "mongodb-sync-standalone"

include("examples:mongodb:mongodb-template")
project(":examples:mongodb:mongodb-template").projectDir = file("examples/mongodb/mongodb-template")
project(":examples:mongodb:mongodb-template").name = "mongodb-template"

/**
 * MYSQL
 */
include("examples:sql:mysql:mysql-springboot")
project(":examples:sql:mysql:mysql-springboot").projectDir = file("examples/sql/mysql/mysql-springboot")
project(":examples:sql:mysql:mysql-springboot").name = "mysql-springboot"

include("examples:sql:mysql:mysql-standalone")
project(":examples:sql:mysql:mysql-standalone").projectDir = file("examples/sql/mysql/mysql-standalone")
project(":examples:sql:mysql:mysql-standalone").name = "mysql-standalone"

/**
 * COUCHBASE
 */
include("examples:couchbase:couchbase-springboot-v2")
project(":examples:couchbase:couchbase-springboot-v2").projectDir = file("examples/couchbase/couchbase-springboot-v2")
project(":examples:couchbase:couchbase-springboot-v2").name = "couchbase-springboot-v2"

include("examples:couchbase:couchbase-standalone")
project(":examples:couchbase:couchbase-standalone").projectDir = file("examples/couchbase/couchbase-standalone")
project(":examples:couchbase:couchbase-standalone").name = "couchbase-standalone"

/**
 * DYNAMODB
 */
include("examples:dynamodb:dynamodb-standalone")
project(":examples:dynamodb:dynamodb-standalone").projectDir = file("examples/dynamodb/dynamodb-standalone")
project(":examples:dynamodb:dynamodb-standalone").name = "dynamodb-standalone"

