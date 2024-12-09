package io.flamingock.examples.dynamodb.standalone.mongock;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class ChangeLog {
    private String changeId;
    private String executionIdAuthor;
    private boolean systemChange;
    private String executionId;
    private String changeSetMethod;
    private String author;
    private String changeLogClass;
    private int executionMillis;
    private String state;
    private String type;
    private long timestamp;
    private String executionHostname;

    // Partition Key
    @DynamoDbPartitionKey
    @DynamoDbAttribute("changeId")
    public String getChangeId() {
        return changeId;
    }
    public void setChangeId(String changeId) {
        this.changeId = changeId;
    }

    // Sort Key
    @DynamoDbSortKey
    @DynamoDbAttribute("executionId#author")
    public String getExecutionIdAuthor() {
        return executionIdAuthor;
    }
    public void setExecutionIdAuthor(String executionIdAuthor) {
        this.executionIdAuthor = executionIdAuthor;
    }

    public boolean isSystemChange() {
        return systemChange;
    }
    public void setSystemChange(boolean systemChange) {
        this.systemChange = systemChange;
    }

    public String getExecutionId() {
        return executionId;
    }
    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getChangeSetMethod() {
        return changeSetMethod;
    }
    public void setChangeSetMethod(String changeSetMethod) {
        this.changeSetMethod = changeSetMethod;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getChangeLogClass() {
        return changeLogClass;
    }
    public void setChangeLogClass(String changeLogClass) {
        this.changeLogClass = changeLogClass;
    }

    public int getExecutionMillis() {
        return executionMillis;
    }
    public void setExecutionMillis(int executionMillis) {
        this.executionMillis = executionMillis;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getExecutionHostname() {
        return executionHostname;
    }
    public void setExecutionHostname(String executionHostname) {
        this.executionHostname = executionHostname;
    }
}

