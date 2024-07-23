getProjects();
const sendImg = document.querySelector('.sendimg');
projectsDisplayInSideBar();

//Retrieve Operation
async function getProjects() {
  try {
    const response = await fetch('/project/projects', {
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
    return null;
  }
}

async function projectsDisplayInSideBar(){
  const data = await getProjects();
  const projects = Object.values(data);
  displayProjects(projects);
  projectsSelectionInInviteBar(projects);
  addEventListenerToIniviteButton();
}


//By Dom displaying
function displayProjects(projects){
  const projectList=document.querySelector('.projects');
  projects.forEach(proj=>{
    const liProjEle=document.createElement('li');
    liProjEle.id = proj.projId;
    liProjEle.textContent = proj.projName;
    projectList.insertBefore(liProjEle,projectList.lastElementChild);
    addEventListenerToProjects(liProjEle);
  });
}

//selected and unSelected functionality
function addEventListenerToProjects(project){

  project.addEventListener('click', () => {

    if(SeletedProjectId) removeSelectedProject(document.getElementById(`${SeletedProjectId}`));

    if(project.id!=SeletedProjectId){
      if(!SeletedProjectId){
            document.querySelector('.main_task_board').style.display = 'grid';
            document.querySelector('.user_inivite_section').style.display = 'none';
            document.querySelector('.main_task_board').innerHTML=`
                <section class="tasks | bigcard">

                  <div class="flex_display">
                    <i class="fa-solid fa-list-check"></i>
                    <h3>Task Name</h3>
                  </div>

                  <ul class="flex_display">
                  </ul>

                </section>

                <section class="assigned_to | bigcard">

                  <div class="flex_display">
                    <i class="fa-solid fa-user-group"></i>
                    <h3>Assign</h3>
                  </div>

                  <ul class="flex_display">
                  </ul>

                </section>

                <section class="progress | bigcard">

                  <div class="flex_display">
                    <i class="fa-solid fa-bars-progress"></i>
                    <h3>Progress</h3>
                  </div>

                  <ul class="flex_display">
                  </ul>
                </section>

                <section class="due_date | bigcard">

                  <div class="flex_display">
                    <i class="fa-regular fa-calendar"></i>
                    <h3>Due</h3>
                    <button class="newTask">+ New Task</button>
                  </div>

                  <ul class="flex_display">
                  </ul>

                </section>`;
            addNewTask();
            }
	  SeletedProjectId=project.id;
      selecteProject(project);
    }else{
      SeletedProjectId='';
      msgBox.innerHTML = '';
      document.querySelector('.main_task_board').style.display = 'none';
      enableUserInviteGird();
    }

  });

}

function selecteProject(project){
  getProjectDetails(SeletedProjectId);
  project.classList.add("selectedproject");
   sendImg.classList.add('canSendMsg');
  sendImg.addEventListener('click',sendContributerMessages);
}

function removeSelectedProject(project){
  if(project.classList.contains("selectedproject")){
    project.classList.remove("selectedproject");
  }
   sendImg.classList.remove('canSendMsg');
   sendImg.removeEventListener('click',sendContributerMessages);
}

//created new Project
const projInp=document.querySelector('input[name="project_title"]');

projInp.addEventListener('keydown',(event)=>{
  if (event.key === 'Enter'){
	  addNewProject(projInp);
  }
});

function addNewProject(projValue){
  if(projValue.value){

    const project={
      'projName' : projValue.value,
    };
    
    projValue.value='';

    fetch('/project/addProject',
      {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(project)
      }
    ).then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.json();
    }).then(projects => {
      displayProjects(projects);
      projectsSelectionInInviteBar(projects);
    }).catch(error => {
      console.error('Fetch error:', error);
    });
  }
}