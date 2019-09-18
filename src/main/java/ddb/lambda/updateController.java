package ddb.lambda;


import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import ddb.lambda.Models.History;
import ddb.lambda.Models.Task;

import java.util.ArrayList;

public class updateController {

    private DynamoDB dynamoDb;
    private String DYNAMODB_TABLE_NAME = "task";
    private Regions REGION = Regions.US_WEST_2;

    public Task saveNewTask(Task task, Context context) {

//        DynamoDB db = new DynamoDB(task.getId());

        ArrayList<History> historyArrayList = new ArrayList<>();
        task.setHistoryArrayList(historyArrayList);
        task.setNewHistory(new History("Task: " + task.getDescription() + " was added"));
        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);


        ddbMapper.save(task);



        return task;
    }
//
//    public Task updateTask(Long Id, Context context) {
//        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
//        DynamoDB dynamoDB = new DynamoDB(client);
//
//        Table table = dynamoDB.getTable("task");
//        Item task = table.getItem("Id", Id);
//
//        return task;
    }




}