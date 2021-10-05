package com.revature.post_records;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostRecordsHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Gson mapper = new GsonBuilder().setPrettyPrinting().create();
    private final RecordsRepository recordRepo;
    private final UserRepository userRepo;

    public PostRecordsHandler(){
        recordRepo = new RecordsRepository();
        userRepo = new UserRepository();
    }

    public PostRecordsHandler(RecordsRepository recordRepo, UserRepository userRepo) {
        this.recordRepo = recordRepo;
        this.userRepo = userRepo;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        LambdaLogger logger = context.getLogger();

        logger.log("RECEIVED EVENT: " + apiGatewayProxyRequestEvent);

        Records newRecords = mapper.fromJson(apiGatewayProxyRequestEvent.getBody(), Records.class);

        recordRepo.addRecord(newRecords);

        for(Player p: newRecords.getPlayerList()){
            User newUser = userRepo.getUserById(p.getId());
            logger.log("Modifying player: " + newUser);
            if(newUser!=null) {
                logger.log("Player earned: " + p.getPoints() + "points. Adding to " + newUser.getPoints());
                newUser.setPoints(newUser.getPoints() + p.getPoints());
                if (p.getPlacing() == 1) {
                    logger.log("Setting wins from " + newUser.getWins());
                    newUser.setWins(newUser.getWins() + 1);
                    logger.log("Wins are now: " + newUser.getWins());
                }
                else {
                    logger.log("Setting losses from " + newUser.getWins());
                    newUser.setLosses(newUser.getLosses() + 1);
                    logger.log("Losses are now: " + newUser.getWins());
                }

                newUser.getGameRecords().add(newRecords.getRecordId());
                userRepo.updateUser(newUser);
            }
        }
        
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization");
        headers.put("Access-Control-Allow-Origin", "*");
        responseEvent.setHeaders(headers);
        responseEvent.setBody(mapper.toJson(newRecords));

        return responseEvent;
    }
}
