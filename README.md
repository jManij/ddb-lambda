# Lab: Lambda with Dynamo

## Overview
- The lab uses API gateways which is an entrypoint of trigger to lambda functions.
- Lambda functions: save a new task, update a task, delete a task.

## How To Test
 **Use postman and provide similar parameters when testing.** 
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
   
###### Update a new task        
  * API GATEWAY: https://phv3cxw8ud.execute-api.us-west-2.amazonaws.com/dev/updatetask
  * Method: PUT
  * PARAMS: JSON OBJECT
           
          {
          
              "id" : new_id_that_you_just_created_from_create_a_new_task
              
              "title" : "New title changed",
              
              "description" : "new description changed",
              
              "status" : "Assigned",
              
              "assignee" : "New assignee changed"
              
              }
              
   * Response: JSON object
   
###### Delete a task        
  * API GATEWAY: https://lpce1u7wt1.execute-api.us-west-2.amazonaws.com/dev/deletetask
  * Method: DELETE
  * PARAMS: STRING ID
  
          * ID_that_you_want_to_delete
          
   * Response: String 
   
   ### FrontEnd Link 
   * GET /tasks
      * https://04kiiaovkl.execute-api.us-west-2.amazonaws.com/dev/tasks
      
   * GET /tasks/{user}
      * https://g6tbnjg2s3.execute-api.us-west-2.amazonaws.com/dev/task/%7Buser%7D
      
   * POST /tasks
      * https://lltrgze6rl.execute-api.us-west-2.amazonaws.com/dev/task
      
   * PUT /tasks/{id}/state
      * https://phv3cxw8ud.execute-api.us-west-2.amazonaws.com/dev/tasks/%7Bid%7D/state
      
   * PUT /tasks/{id}/assign/{assignee}
      * https://7a6kr231di.execute-api.us-west-2.amazonaws.com/dev/tasks/{id}/assign/{assignee}
      
              
## Credits and contributions
  * ### Manish
  * ### Roman
  * ### Jack
  * ### Fabian

