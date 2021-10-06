package com.revature.post_records;

import com.revature.exceptions.ResourceNotFoundException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {

    private final DynamoDbTable<User> userTable;

    public UserRepository(){
        DynamoDbClient db = DynamoDbClient.builder().httpClient(ApacheHttpClient.create()).build();
        DynamoDbEnhancedClient dbClient = DynamoDbEnhancedClient.builder().dynamoDbClient(db).build();
        userTable = dbClient.table("Users", TableSchema.fromBean(User.class));
    }
    public User getUser(String name) {
        AttributeValue val = AttributeValue.builder().s(name).build();
        Expression filter = Expression.builder().expression("#a = :b") .putExpressionName("#a", "username") .putExpressionValue(":b", val).build();
        ScanEnhancedRequest request = ScanEnhancedRequest.builder().filterExpression(filter).build();

        User user = userTable.scan(request).items().stream().findFirst().orElseThrow(ResourceNotFoundException::new);
        return user;
    }

    public User getUserById(String id){

        AttributeValue val = AttributeValue.builder().s(id).build();
        Expression filter = Expression.builder().expression("#a = :b") .putExpressionName("#a", "id") .putExpressionValue(":b", val).build();
        ScanEnhancedRequest request = ScanEnhancedRequest.builder().filterExpression(filter).build();

        try {
            return userTable.scan(request).stream().findFirst().orElseThrow(ResourceNotFoundException::new).items().get(0);
        } catch (Exception e){
            System.out.println(e);
        }
        System.out.println("EXCEPTION: Returning null");
        return null;
    }

    public User updateUser(User user){
        try {
            return userTable.updateItem(user);
        }catch (Exception e){
            return null;
        }
    }


    public User addSet(Set newSet, User user){
        //Create a UserSetDoc with correct Set data
        User.UserSetDoc doc = new User.UserSetDoc(newSet);

        if(user.getCreatedSets() == null) {
            user.setCreatedSets(new ArrayList<User.UserSetDoc>());
        }

        //Create a copy of created_sets
        List<User.UserSetDoc> temp = user.getCreatedSets();
        //Modify copy
        temp.add(doc);
        //Save copy
        user.setCreatedSets(temp);

        //Save user with updated fields to db
        user = userTable.updateItem(user);

        return user;
    }
}