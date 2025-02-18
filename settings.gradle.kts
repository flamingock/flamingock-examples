
rootProject.name = "flamingock-examples"

include("mongodb:mongodb-springboot-springdata")
project(":mongodb:mongodb-springboot-springdata").projectDir = file("mongodb/mongodb-springboot-springdata")
project(":mongodb:mongodb-springboot-springdata").name = "mongodb-springboot-springdata"

include("mongodb:mongodb-springboot-sync")
project(":mongodb:mongodb-springboot-sync").projectDir = file("mongodb/mongodb-springboot-sync")
project(":mongodb:mongodb-springboot-sync").name = "mongodb-springboot-sync"

include("mongodb:mongodb-springboot-v3-springdata-v4")
project(":mongodb:mongodb-springboot-v3-springdata-v4").projectDir = file("mongodb/mongodb-springboot-v3-springdata-v4")
project(":mongodb:mongodb-springboot-v3-springdata-v4").name = "mongodb-springboot-v3-springdata-v4"

include("mongodb:mongodb-sync-standalone")
project(":mongodb:mongodb-sync-standalone").projectDir = file("mongodb/mongodb-sync-standalone")
project(":mongodb:mongodb-sync-standalone").name = "mongodb-sync-standalone"




include("mysql-springboot")
include("mysql-standalone")

include("couchbase-springboot-v2")
include("couchbase-standalone")

include("dynamodb:dynamodb-standalone")
val dynamoDbProject = project(":dynamodb:dynamodb-standalone")
dynamoDbProject.projectDir = file("dynamodb/dynamodb-standalone")
dynamoDbProject.name = "dynamodb-standalone"
