package com.revature.post_records;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Data
@DynamoDbBean
public class Player {

    private String id;
    private String name;
    private int placing;
    private int points;

}
