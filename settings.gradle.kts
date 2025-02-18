
rootProject.name = "flamingock-examples"
include("mongodb-springboot-springdata")
include("mongodb-springboot-sync")
include("mongodb-springboot-v3-springdata-v4")
include("mongodb-sync-standalone")

include("mysql-springboot")
include("mysql-standalone")

include("couchbase-springboot-v2")
include("couchbase-standalone")

include("dynamodb:dynamodb-standalone")
val dynamoDbProject = project(":dynamodb:dynamodb-standalone")
dynamoDbProject.projectDir = file("dynamodb/dynamodb-standalone")
dynamoDbProject.name = "dynamodb-standalone"
