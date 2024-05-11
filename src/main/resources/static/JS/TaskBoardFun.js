let SeletedProjectId;
let options=[];

//RETRIEVE(READ) Project Report details
function getProjectDetails(projectID){
  fetch(`/projectReport/projectDetails/${projectID}`,
  {
      method: 'GET',
      headers: {
      'Content-Type': 'application/json'
      }
  }).then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }else if(response.status===404){
        showErrorMesgNotFoun();
      }else if(response.status===500){
        showServerError();
      }else{
        return response.json();
      }
  }).then(projectsDetailsData => {
    options=[];
    document.querySelector('.tasks > ul').innerHTML='';
    document.querySelector('.assigned_to > ul').innerHTML='';
    document.querySelector('.progress > ul').innerHTML='';
    document.querySelector('.due_date > ul').innerHTML='';
    document.querySelector('.contributor>ul').innerHTML='';
    document.querySelector('.taskForm #assign-user').innerHTML='';
    contributerDisplay(Object.values(projectsDetailsData));
    displayProjectsDetailsData(Object.values(projectsDetailsData));
  }).catch(error => {
      console.error('Fetch error:', error);
  });
}

function showServerError(){
  alert("server error unable to fetch");
}

function showErrorMesgNotFoun(){
  alert("Not able ")
}

//by DOM display
function displayProjectsDetailsData(projectsDetailsDataArray) {
  // Obtain references to existing lists
  const taskList = document.querySelector('.tasks > ul');
  const assignedToList = document.querySelector('.assigned_to > ul');
  const progressList = document.querySelector('.progress > ul');
  const dueDateList = document.querySelector('.due_date > ul');
  const assignInp=document.querySelector('.taskForm #assign-user');

  projectsDetailsDataArray.forEach(projectsDetails => {

    const taskListItem = createListItem('card flex_display', projectsDetails.projRepID,
    'tasktittle',projectsDetails.taskTittle);

    const assignedToListItem = createListItem('flex_display card',projectsDetails.projRepID,
    'assign',`<div class="user_circle orange_circle"></div> <span>${projectsDetails.assign} </span>`);

    const progressListItem = createListItem('card flex_display', projectsDetails.projRepID,
    'progress', `<span>${projectsDetails.progress}</span>`);

    const dueDateListItem = createListItem('card flex_display', projectsDetails.projRepID,
    'due', `
    ${dateFormatted(projectsDetails.dueDate)}

    <i class="fa-solid fa-ellipsis-vertical"></i>

    <div class="more">

      <div class="remove">
        <i class="fa-solid fa-trash | delete"></i>
        <span>remove</span>
      </div>

      <div class="complete">
        <i class="fa-solid fa-check"></i>
        <span>complete</span>
      </div>

    </div>`);

    const optionElement = document.createElement('option');
    optionElement.innerHTML = `${projectsDetails.assign}`;
    optionElement.value = projectsDetails.assign;

    options.push(projectsDetails.assign);

    taskList.appendChild(taskListItem,taskList.lastElementChild);
    assignedToList.appendChild(assignedToListItem,assignedToList.lastElementChild);
    progressList.appendChild(progressListItem,progressList.lastElementChild);
    dueDateList.appendChild(dueDateListItem,dueDateList.lastElementChild);
    assignInp.appendChild(optionElement);
    addEventListenerToCard(taskListItem);
    addEventListenerToCard(progressListItem);
    addEventListenerToCard(assignedToListItem);
    deleteAddEventLister(dueDateListItem.querySelector('.more .remove'));
    completeEvent(dueDateListItem.querySelector('.more .complete'));
    moreFun(dueDateListItem.querySelector('.fa-ellipsis-vertical'));
  });
}

//helps to create element in displaying
function createListItem(classes, id, column, content) {
  const newListItem = document.createElement('li');
  newListItem.classList.add(...classes.split(' '));
  if (column) {
     newListItem.setAttribute('column',column);
  }
  if (content) {
    newListItem.innerHTML = content;
  }
  if(id){
	  newListItem.setAttribute('projRepId',id);
  }
  return newListItem;
}

//helps in displaying formatted date
function dateFormatted(date){
  let arrDate=date.split('-');
  switch(arrDate[1]){
    case '01': return arrDate[2]+'-Jan-'+arrDate[0];
    case '02': return arrDate[2]+'-Feb-'+arrDate[0];
    case '03': return arrDate[2]+'-Mar-'+arrDate[0];
    case '12': return arrDate[2]+'-Dec-'+arrDate[0];
    case '04': return arrDate[2]+'-Apr-'+arrDate[0];
    case '05': return arrDate[2]+'-May-'+arrDate[0];
    case '06': return arrDate[2]+'-Jun-'+arrDate[0];
    case '07': return arrDate[2]+'-July-'+arrDate[0];
    case '08': return arrDate[2]+'-Aug-'+arrDate[0];
    case '09': return arrDate[2]+'-Sept-'+arrDate[0];
    case '10': return arrDate[2]+'-Oct-'+arrDate[0];
    case '11': return arrDate[2]+'-Nov-'+arrDate[0];
  }
}


/* event to task and progress,assign column li element to input element to UPDATE details*/
async function updateProjDetails(projRepId,column,upVlaue){

  let updateValue={
    projRepId,
    column,
    upVlaue
  };
  console.log(updateValue);

  return await fetch('/projectReport/update',

    {	method: 'PATCH',
      headers: {
        'Content-Type': 'application/json'
    	},
    	body: JSON.stringify(updateValue)
    }
    ).then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return true;
    }).then(updated => {
        return updated;
    }).catch(error => {
        return false;
    });

}

function addEventListenerToCard(card){

  card.addEventListener("dblclick",()=>{
    cardToInput(card);
  });

}

function cardToInput(card){

  let inputElement;

  if(card.getAttribute('column')==='assign'){
    inputElement= document.createElement('select');
    inputElement.name = 'assign-user';
    inputElement.id = 'assign-user';
    options.forEach(optionText => {
      const option = document.createElement('option');
      option.value = optionText;
      option.textContent = optionText;
      inputElement.appendChild(option);
    });
  }else{
    inputElement=document.createElement('input');
    inputElement.type='text';
  }

  inputElement.value=card.textContent.trim();
  inputElement.classList.add(...card.classList);
  inputElement.classList.add('inputcard');
  inputElement.addEventListener("focusout",()=>{
    inputToCard(inputElement,card);
  });
  card.replaceWith(inputElement);

}

function inputToCard(inputElement,liElement){
  let flag=updateProjDetails(liElement.getAttribute('projRepId'),liElement.getAttribute('column'),inputElement.value);
  flag.then((updated)=>{
	  if(updated){
      if(liElement.getAttribute('column')==='tasktittle'){
        liElement.innerHTML=inputElement.value;
      }else if(liElement.getAttribute('column')==='assign'){
        liElement.innerHTML=`
        <div class="user_circle orange_circle"></div>
        <span>${inputElement.value}</span>
        `;
      }else{
        liElement.innerHTML=`
        <span>${inputElement.value}</span>
        `;
      }
    }
  });
  inputElement.replaceWith(liElement);
}


//Event to CREATE new Project Report Details
async function newProjDetails(){

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
  console.log(ProjectReport);
  console.log(SeletedProjectId);
  if(taskValue.value&&assignValue.value&&progressValue.value&&dueDateValue.value){

 	taskValue.value='';
    assignValue.value='';
    progressValue.value='';
    dueDateValue.value='';
    await fetch('/projectReport/insert',

      {	method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(ProjectReport)
      }
      ).then(response => {
        if (response.status===201) {
          return response.json();
        }else{
          throw new Error('Network response was not ok');
        }
      }).then(projectsDetailsData => {
        console.log(projectsDetailsData);
        contributerDisplay(Object.values(projectsDetailsData));
        displayProjectsDetailsData(Object.values(projectsDetailsData));
      }).catch(error => {
          console.error('Fetch error:', error);
      });

  }else{
    alert("Enter the Data in All Input Field");
  }

}

function addNewTask(){
  document.querySelector('.taskForm button')
            .addEventListener('click',()=>{
              newProjDetails();
            });

  const newTaskForm=document.querySelector('.taskFormdiv');
  document.querySelector('.due_date .newTask')
          .addEventListener('click',()=>{
            document.body.classList.add('bodyadd');
            newTaskForm.style.display='block';
          });

  document.querySelector('.taskFormdiv .taskheading .fa-xmark')
          .addEventListener('click',()=>{
            newTaskForm.style.display='none';
            document.body.classList.remove('bodyadd');
          });
}


//DELETE particular Project Report Details
async function deleteBackendCall(ProjReportDelete){

  return await fetch('/projectReport/deleteProjRep',
    {
      method: 'DELETE',
      headers: {
      	'Content-Type': 'application/json'
       },
      body:JSON.stringify(ProjReportDelete.getAttribute('projRepId'))
    }).then(response => {
        if (!response.ok) {
          return false;
        }else{
          return true;
        }
    }).then( success=> {
      return success;
    }).catch(fails => {
      return fails;
    });
}

function deleteAddEventLister(deleteItem) {
  deleteItem.addEventListener('click',()=>{
    const flag=deleteBackendCall(deleteItem.parentElement.parentElement);
   	flag.then(deleted=>{
		  if(deleted){
        const proRepId=deleteItem.parentElement.parentElement.getAttribute('projRepId');
        const elementsWithProjRepId = document.querySelectorAll(`[projrepid="${proRepId}"]`);
        elementsWithProjRepId.forEach(element => {
            element.remove();
        });
      }
	  });
  });
}


//more section functionality
function moreFun(more){
  more.nextElementSibling.style.display='none';
  more.addEventListener('click',()=>{
    if(more.nextElementSibling.style.display==='none'){
      more.nextElementSibling.style.display='block';
    }else{
      more.nextElementSibling.style.display='none';
    }
  });

}


//completed Task
function completeEvent(completeEle){

  completeEle.addEventListener('click',()=>{

    let projRepId=completeEle.parentElement.parentElement.getAttribute('projrepid');
    document.querySelectorAll(`[projrepid="${projRepId}"]`).forEach(ele=>{
      ele.style.textDecoration = "line-through";
    });

  });

}