package ddb.lambda;


import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import ddb.lambda.Models.History;
import ddb.lambda.Models.States;
import ddb.lambda.Models.Task;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class updateController {

    public List<Task> getAllTasks(Context context) {
        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);
        List<Task> iList = ddbMapper.scan(Task.class, new DynamoDBScanExpression());

        return iList;
    }



    public APIGatewayProxyResponseEvent assign(APIGatewayProxyRequestEvent event) {

        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);
        StringBuilder id = new StringBuilder();
        StringBuilder assignee = new StringBuilder();

        for (Map.Entry<String,String> entry : event.getPathParameters().entrySet()) {
            if (entry.getKey() == "id") {
                id.append(entry.getValue());
            } else {
                assignee.append(entry.getValue());
            }
        }
        Task task = ddbMapper.load(Task.class, id.toString());
        task.setAssignee(assignee.toString());
        task.setStatus("Assigned");
        task.getHistoryArrayList().add(new History(assignee + " assigned to the task"));
        ddbMapper.save(task);

        Gson gson = new Gson();
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        responseEvent.setBody(gson.toJson(task));


        return responseEvent;

    }



    public APIGatewayProxyResponseEvent getTaskForAUser(APIGatewayProxyRequestEvent event) {

        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);

        StringBuilder path = new StringBuilder();

        for (Map.Entry<String,String> entry : event.getPathParameters().entrySet())
            path.append(entry.getValue());



        HashMap<String, AttributeValue> expressionAttributeValues =
                new HashMap<String, AttributeValue>();
        expressionAttributeValues.put(":val", new AttributeValue().withS(path.toString()));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().
                withFilterExpression("assignee = :val")
                .withExpressionAttributeValues(expressionAttributeValues);


        List<Task> iList = ddbMapper.scan(Task.class, scanExpression);

        Gson gson = new Gson();
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        responseEvent.setBody(gson.toJson(iList));


        return responseEvent;

    }

    public Task saveNewTask(Task task, Context context) {

        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);
        Task t = new Task();
        if (task.getAssignee() != null) {
            t.setAssignee(task.getAssignee());
            t.setStatus("Assigned");
        } else {
            t.setAssignee("");
            t.setStatus("Available");
        }
        t.setTitle(task.getTitle());
        t.setDescription(task.getDescription());
        ArrayList<History> historyArrayList = new ArrayList<>();
        t.setHistoryArrayList(historyArrayList);
        t.setNewHistory(new History("Task: " + t.getDescription() + " was added"));
        ddbMapper.save(t);


        return t;
    }



    public APIGatewayProxyResponseEvent updateTask(APIGatewayProxyRequestEvent event) {

        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);
        StringBuilder path = new StringBuilder();

        for (Map.Entry<String,String> entry : event.getPathParameters().entrySet())
            path.append(entry.getValue());

        Task oldTask = ddbMapper.load(Task.class, path.toString());
        StringBuilder message = new StringBuilder("Task advanced from " + oldTask.getStatus()); // For displaying message
        oldTask.setStatus(States.changeState(oldTask.getStatus())); //Changes the current status
        message.append(" to "+ oldTask.getStatus());

        oldTask.getHistoryArrayList().add(new History(message.toString()));
        ddbMapper.save(oldTask);

        Gson gson = new Gson();
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        responseEvent.setBody(gson.toJson(oldTask));


        return responseEvent;
    }

    public APIGatewayProxyResponseEvent deleteTask(APIGatewayProxyRequestEvent event) {
        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);

        StringBuilder ID = new StringBuilder();

        for (Map.Entry<String,String> entry : event.getPathParameters().entrySet())
            ID.append(entry.getValue());

        Task task = ddbMapper.load(Task.class, ID.toString());
        Gson gson = new Gson();
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        responseEvent.setBody(gson.toJson("The following object has been deleted: \n" +
                "ID: " + task.getId() +
                " \n Task Name: " + task.getTitle()));
        ddbMapper.delete(task);

        return  responseEvent;

    }


}
