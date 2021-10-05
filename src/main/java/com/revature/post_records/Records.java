package com.revature.post_records;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.Date;
import java.util.List;

@Data
@DynamoDbBean
public class Records {


    private String recordId;

    private List<Player> playerList;

    private boolean winner;

    private String datePlayed;

    private List<Card> cardList;

    @DynamoDbPartitionKey
    public String getRecordId() {
        return recordId;
    }
}


