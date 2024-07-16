
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
function newProjDetails(){

  const parentElement=document.querySelector('.tasks>ul');
  const id=parentElement.querySelector(":nth-child("+(parentElement.children.length)+")")?.getAttribute('projRepId');
  const ProjRepID= id!=null ? (Number(id)+0.1).toFixed(2) : (Number(SeletedProjectId)+0.1).toFixed(2);

  let taskValue=document.querySelector('.taskForm #task-title');
  let assignValue=document.querySelector('.taskForm #assign-user');
  let progressValue=document.querySelector('.taskForm #progress');
  let dueDateValue=document.querySelector('.taskForm #due-date');

  const ProjectReport={
    'projRepID':ProjRepID,
    'taskTittle':taskValue.value,
    'assign':assignValue.value,
    'progress':progressValue.value,
    'dueDate':dueDateValue.value
  };

  if(taskValue.value&&dueDateValue.value){

 	taskValue.value='';
    assignValue.value='';
    progressValue.value='';
    dueDateValue.value='';

    sendMessage("/insert",ProjectReport);

  }else{
    alert("Enter the Data in All Input Field");
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

