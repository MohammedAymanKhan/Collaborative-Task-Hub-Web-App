
const socket=new WebSocket("ws://localhost:8080/projectReports");

socket.onopen = function(e) {
    console.log('connected');
}

socket.onmessage = function(event) {

  let data=JSON.parse(event.data);
  switch(data.header){
    case "received": onMessageProjectsDisplay(data.body);
        break;
    case "inserted" : onMessageDisplayNewProjectDetails(data.body);
        break;
    case "updated" : onMessageUpdatedValue(data.body);
        break;
    case "deleted" : onMessageDeleteElement(data.body);
        break;
    case "msgReceived" :  displayProjectsMessages(data.body);
        break;
    case "messageSendByMe" : displayMessageSendByMe(data.body);
        break;
    case "msgDeleted" :  deletedChatMessage(data.body);
        break;
    case "updateMsgGot" : updateMsgFromGroup(data.body);
        break;
    case "error" :  errorMessageDisplay(data.body);
        break;
  }

};

//socket.close(1000, "Work complete");

function sendMessage(urlRequest,requestBody){

  let request={
    header : urlRequest,
    requestBody
  }
  socket.send(JSON.stringify(request));
}

//RETRIEVE(READ) Project Report details
function getProjectDetails(projectID){
  sendMessage('/subscribe',projectID);
}
function onMessageProjectsDisplay(projectsDetailsData){
  options=[];
  document.querySelector('.tasks > ul').innerHTML='';
  document.querySelector('.assigned_to > ul').innerHTML='';
  document.querySelector('.progress > ul').innerHTML='';
  document.querySelector('.due_date > ul').innerHTML='';
  document.querySelector('.contributor>ul').innerHTML='';
  document.querySelector('.taskForm #assign-user').innerHTML='';
  displayProjectsDetailsData(Object.values(projectsDetailsData));
}


//Event to CREATE new Project Report Details
function newProjDetails() {

  if (projectReport.taskTitle && !projectReport.dueDate) {
    // Get references to the form elements
    const taskTitleElement = document.querySelector('.taskForm #task-title');
    const assignUserElement = document.querySelector('.taskForm #assign-user');
    const progressElement = document.querySelector('.taskForm #progress');
    const dueDateElement = document.querySelector('.taskForm #due-date');

    // Generate a new project report ID
    const parentElement = document.querySelector('.tasks > ul');
    const lastChild = parentElement.querySelector(':last-child');
    const lastProjRepID = lastChild ? Number(lastChild.getAttribute('projRepId')) : Number(SeletedProjectId);
    const projRepID = (lastProjRepID + 0.1).toFixed(2);

    // Construct the ProjectReport object
    const projectReport = {
        projRepID: projRepID,
        taskTitle: taskTitleElement.value.trim() || null,
        assign: assignUserElement.value.trim() || null,
        progress: progressElement.value.trim() || null,
        dueDate: dueDateElement.value.trim() || null
    };

    // Clear the form fields
    taskTitleElement.value = '';
    assignUserElement.value = '';
    progressElement.value = '';
    dueDateElement.value = '';

    // Send the project report
    sendMessage('/insert', projectReport);

  } else{
    // Check required fields
    alert('Please fill in the required fields: Task Title and Due Date.');

  }

}
function onMessageDisplayNewProjectDetails(projectsDetailsData) {
  displayProjectsDetailsData(Object.values([projectsDetailsData]));
}

/* event to task and progress,assign column li element to input element to UPDATE details */
function updateProjDetails(projRepId,column,upValue){

  let updateValue={
    projRepId,
    column,
    upValue
  };
  console.log(updateValue);
  sendMessage("/update",updateValue);

}
function onMessageUpdatedValue(updatedData){
console.log(updatedData);
  cardElement=document.querySelector(`[projrepid="${updatedData.projRepId}"][column="${updatedData.column}"]`);
  if(cardElement.getAttribute('column')==='tasktittle'){
    cardElement.innerHTML=updatedData.upValue;
  }else if(cardElement.getAttribute('column')==='assign'){
    cardElement.innerHTML=`<div class="user_circle orange_circle"></div>
    <span>${updatedData.upValue}</span>`;
  }else{
    cardElement.innerHTML=`<span>${updatedData.upValue}</span>`;
  }
}

//DELETE particular Project Report Details
function deleteBackendCall(ProjReportDelete){
  sendMessage("/delete",ProjReportDelete.getAttribute('projRepId'))
}
function onMessageDeleteElement(projRepID){
  const elementsWithProjRepId = document.querySelectorAll(`[projrepid="${projRepID}"]`);
  elementsWithProjRepId.forEach(element => {
    element.remove();
  });
}

// Error Message Display
function errorMessageDisplay(errorMessage){
  const errorElement=document.querySelector(".error");
  errorElement.querySelector('.error-message').innerHTML=errorMessage;
  errorElement.style.display = "flex";
  const errorSetTimeout=setTimeout(()=>{
    errorElement.style.display = "none";
    clearInterval(errorSetInterval);
  },3000);
}

