package com.revature.post_records;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class RecordsRepository {
    private final DynamoDbTable<Records> recordTable;

    public RecordsRepository() {
        DynamoDbClient db = DynamoDbClient.builder()
            .httpClient(ApacheHttpClient.create())
            .build();

        DynamoDbEnhancedClient dbClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(db)
                .build();

        recordTable = dbClient.table("Records", TableSchema.fromBean(Records.class));
    }

    public void addRecord(Records newRecord){
        recordTable.putItem(newRecord);
    }
}
