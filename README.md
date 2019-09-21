# Lab: Lambda with Dynamo

## Overview
- The lab uses API gateways which is an entrypoint of trigger to lambda functions.
- Lambda functions: create a new task, get tasks for specific user, save a new task, update a task state,
  change assignee to the task, delete a task.

## How To Test
 **Use postman and provide similar parameters when testing.** 
 ###### Get All Tasks
  * API GATEWAY: https://04kiiaovkl.execute-api.us-west-2.amazonaws.com/dev/tasks
  * Method: GET           
  * Response: JSON object
  
###### Create a new task
  * API GATEWAY: https://lltrgze6rl.execute-api.us-west-2.amazonaws.com/dev/task
  * Method: POST
  * PARAMS: JSON OBJECT
  
          
          {
          
              "title" : "New title",
              
              "description" : "new description",
              
              "status" : "Available",
              
              "assignee" : "New assignee"
              
              }
              
   * Response: JSON object
   
 ###### Get tasks for a specific user        
  * API GATEWAY:https://g6tbnjg2s3.execute-api.us-west-2.amazonaws.com/dev/task/{user}
  * Method: GET /task/{user}
  * PARAMS: "assignee" value  of the object as an URL param           
  * Response: JSON object
   
###### Update a task        
  * API GATEWAY:https://phv3cxw8ud.execute-api.us-west-2.amazonaws.com/dev/tasks/{id}/state
  * Method: PUT /tasks/{id}/state
  * PARAMS: id of the object as an URL param           
  * Response: JSON object
  
###### Assign a new user to the task        
  * API GATEWAY:https://7a6kr231di.execute-api.us-west-2.amazonaws.com/dev/tasks/{id}/assign/{assignee}
  * Method: PUT /tasks/{id}/assign/{assignee}
  * PARAMS: id of the object and assignee for the task as URL params         
  * Response: JSON object
  
###### Delete a task        
  * API GATEWAY: https://lpce1u7wt1.execute-api.us-west-2.amazonaws.com/dev/deletetask/{id}
  * Method: DELETE
  * PARAMS: id of task to be deleted as the URL param          
  * Response: Status of delte as a String
   
 ### FrontEnd Link 
      
              
## Credits and contributions
  * ### Manish
  * ### Roman
  * ### Jack
  * ### Fabian

