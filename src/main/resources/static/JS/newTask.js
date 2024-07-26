function addNewTask(){

  document.querySelector('.taskForm button').addEventListener('click',(event)=>{
    event.preventDefault();
    newProjDetails();
  });

  const newTaskForm = document.querySelector('.taskFormdiv');

  document.querySelector('.due_date .newTask').addEventListener('click',()=>{
    document.body.classList.add('bodyadd');
    populateAssignOptions(document.querySelector('.taskForm #assign-user'), SeletedProjectId);
    newTaskForm.style.display='block';
  });

  document.querySelector('.taskFormdiv .taskheading .fa-xmark').addEventListener('click',()=>{
    newTaskForm.style.display='none';
    document.body.classList.remove('bodyadd');
  });
}

