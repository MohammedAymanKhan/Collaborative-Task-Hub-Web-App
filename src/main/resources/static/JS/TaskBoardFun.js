let SeletedProjectId;

//by DOM display
function displayProjectsDetailsData(projectsDetailsDataArray) {
  // Obtain references to existing lists
  const taskList = document.querySelector('.tasks > ul');
  const assignedToList = document.querySelector('.assigned_to > ul');
  const progressList = document.querySelector('.progress > ul');
  const dueDateList = document.querySelector('.due_date > ul');

  projectsDetailsDataArray.forEach(projectsDetails => {

    const taskListItem = createListItem('card flex_display', projectsDetails.projRepID,
    'tasktittle',projectsDetails.taskTittle);

    const assignedToListItem = createListItem('flex_display card',projectsDetails.projRepID,
    'assign',`<div class="user_circle orange_circle"></div> <span>${projectsDetails.assign} </span>`);

    const progressListItem = createListItem('card flex_display', projectsDetails.projRepID,
    'progress', `<span>${projectsDetails.progress}</span>`);

    const dueDateListItem = createListItem('card flex_display', projectsDetails.projRepID,
    'due', `
    ${projectsDetails.dueDate}

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

    taskList.appendChild(taskListItem,taskList.lastElementChild);
    assignedToList.appendChild(assignedToListItem,assignedToList.lastElementChild);
    progressList.appendChild(progressListItem,progressList.lastElementChild);
    dueDateList.appendChild(dueDateListItem,dueDateList.lastElementChild);
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


function addEventListenerToCard(card){

  card.addEventListener("dblclick",()=>{
    cardToInput(card);
  });

}

async function getAssigns(projRepId) {
  try {
    const response = await fetch(`/project/getAssigns/${projRepId}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Fetch error:', error);
    return [];
  }
}

async function populateAssignOptions(inputElement, projRepId) {
  const assignOptions = await getAssigns(projRepId);

  inputElement.innerHTML = '';

  const defaultOption = document.createElement('option');
  defaultOption.value = '';
  defaultOption.textContent = 'Pending Assignment';
  defaultOption.selected = true;
  inputElement.appendChild(defaultOption);

  assignOptions.forEach(optionText => {
    const option = document.createElement('option');
    option.value = optionText.user_id;
    option.textContent = optionText.assign;
    inputElement.appendChild(option);
  });
}

function cardToInput(card){

  let inputElement;

  if(card.getAttribute('column')==='assign'){
    inputElement = document.createElement('select');
    inputElement.name = 'assign-user';
    inputElement.id = 'assign-user';
    populateAssignOptions(inputElement, SeletedProjectId);
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
  let assignName = null;
  if(liElement.getAttribute('column') === 'assign'){
      const selectedOption = inputElement.options[inputElement.selectedIndex];
      assignName = selectedOption.innerText;
  }

  updateProjDetails(liElement.getAttribute('projRepId'),liElement.getAttribute('column'),inputElement.value,assignName);
  inputElement.replaceWith(liElement);

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