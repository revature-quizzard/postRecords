package com.revature.post_records;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Data
@DynamoDbBean
public class Card {
    private String id;
    private String question;
    private String correctAnswer;
}
