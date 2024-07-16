let SeletedProjectId;
let options=[];


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

  updateProjDetails(liElement.getAttribute('projRepId'),liElement.getAttribute('column'),inputElement.value);
  inputElement.replaceWith(liElement);

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

// adding delete Event Listener
function deleteAddEventLister(deleteItem) {
    deleteItem.addEventListener('click',()=>{
      deleteBackendCall(deleteItem.parentElement.parentElement);
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