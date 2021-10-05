package com.revature.post_records;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;

public class RecordsRepository {
    private final DynamoDBMapper dbReader;

    public RecordsRepository() { dbReader = new DynamoDBMapper(AmazonDynamoDBClientBuilder.defaultClient()); }

    public void addRecord(Records newRecord){
        dbReader.save(newRecord, new DynamoDBSaveExpression());
    }
}
