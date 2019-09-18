package ddb.lambda;


import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.lambda.runtime.Context;
import ddb.lambda.Models.History;
import ddb.lambda.Models.Task;

import java.util.ArrayList;


public class updateController {

    private DynamoDB dynamoDb;
    private String DYNAMODB_TABLE_NAME = "task";
    private Regions REGION = Regions.US_WEST_2;

    public Task saveNewTask(Task task, Context context) {

        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);

            ArrayList<History> historyArrayList = new ArrayList<>();
            task.setHistoryArrayList(historyArrayList);
            task.setNewHistory(new History("Task: " + task.getDescription() + " was added"));
            Task t = ddbMapper.load(Task.class, task.getId());
            ddbMapper.save(task);

        return task;
    }

    public Task updateTask(Task task, Context context) {

        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);

        Task oldTask = ddbMapper.load(Task.class, task.getId());
        ArrayList<History> historyArrayList = oldTask.getHistoryArrayList();
        historyArrayList.add(new History("Task was updated"));
        task.setHistoryArrayList(historyArrayList);

        /**
         * Copy the task to old Task
         */
        oldTask = task;
        ddbMapper.save(oldTask);

        return oldTask;
    }

}
