package ddb.lambda;


import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import ddb.lambda.Models.History;
import ddb.lambda.Models.States;
import ddb.lambda.Models.Task;
import org.checkerframework.common.reflection.qual.GetMethod;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class updateController {

    private DynamoDB dynamoDb;
    private String DYNAMODB_TABLE_NAME = "task";
    private Regions REGION = Regions.US_WEST_2;


    public List<Task> getAllTasks(Context context) {
        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);
        List<Task> iList = ddbMapper.scan(Task.class, new DynamoDBScanExpression());

        return iList;
    }


    public List<Task> getTaskForAUser(String username) {

        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);


        HashMap<String, AttributeValue> expressionAttributeValues =
                new HashMap<String, AttributeValue>();
        expressionAttributeValues.put(":val", new AttributeValue().withS(username));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().
                withFilterExpression("assignee = :val")
                .withExpressionAttributeValues(expressionAttributeValues);


        List<Task> iList = ddbMapper.scan(Task.class, scanExpression);

        return iList;

    }

    public Task saveNewTask(Task task, Context context) {

        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);

        ArrayList<History> historyArrayList = new ArrayList<>();
        task.setHistoryArrayList(historyArrayList);
        task.setNewHistory(new History("Task: " + task.getDescription() + " was added"));
        ddbMapper.save(task);

        return task;
    }

    public Task updateTask(String id, Context context) {

        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);

        Task oldTask = ddbMapper.load(Task.class, id);
        StringBuilder message = new StringBuilder("Task advanced from " + oldTask.getStatus()); // For displaying message
        oldTask.setStatus(States.changeState(oldTask.getStatus())); //Changes the current status
        message.append(" to "+ oldTask.getStatus());

        oldTask.getHistoryArrayList().add(new History(message.toString()));
        ddbMapper.save(oldTask);

        return oldTask;
    }

    public String deleteTask(String ID, Context context) {
        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);
        Task task = ddbMapper.load(Task.class, ID);
        ddbMapper.delete(task);
        return task.getDescription() + "[id: " + task.getId() + "] has been deleted successfully!";
    }


}
