
async function searchUser(userDetails){

  let searchByUrl = '';

  let searchDetails = userDetails.split(':');

  switch(searchDetails[0]){
    case 'byEmail': searchByUrl = `/getUsers/byEmail/${searchDetails[1]}`
      break;

    case 'byTechStack': searchByUrl = `/getUsers/byTechStack/${searchDetails[1]}`
      break;
        
    case 'byName': searchByUrl = `/getUsers/byName/${searchDetails[1]}`
      break;

    default : searchByUrl = `/getUsers/searchUser/${searchDetails[0]}`
  }

  fetch(searchByUrl ,
    {
      method: 'GET',
      headers: {
      'Content-Type': 'application/json'
      }
    }).then(response => {
      if (!response.ok) {
        throw new Error("somthing went wroung");
      }else{
        return response.json();
      }
    }).then(usersList => {
      document.querySelector('.user_display_section').innerHTML = '';
      displayUsersSearchList(usersList);
    }).catch(error => {
      console.error(error);
    });
}

function addEventListenerToIniviteButton(){
  document.querySelector('.search_user_by_deatils_section .search_icon_img')
    .addEventListener('click',()=>{
      let searchByValue=document.querySelector('#search_user_by_deatils').innerText;
      searchUser(searchByValue);
    });
}

function getSelectedProjectId() {
  const projectSelect = document.getElementById('proj_selection');
  const selectedOption = projectSelect.options[projectSelect.selectedIndex];
  return selectedOption.getAttribute('projId');
}

function displayUsersSearchList(usersList) {
  const userDisplaySection = document.querySelector('.user_display_section');

  usersList.forEach(user => {
    const userDiv = document.createElement('div');
    userDiv.className = 'user_profile_section';
    userDiv.setAttribute('user_id', user.user_id);

    const userShortName = `${user.first_name[0].toUpperCase()}${user.last_name[0].toUpperCase()}`;
    const techStacks = user.userDetails?.techStacks?.map(stack => `<li>${stack}</li>`).join('') || '';
    const gitHubProjects = Object.entries(user.userDetails?.gitHubProjects || {})
                                  .map(([project, url]) => `<li><a href="${url}" target="_blank">${project}</a></li>`)
                                  .join('') || '';

    userDiv.innerHTML = `
      <div class="user_profile | flex_display">
        <div class="user_profile_cricle | flex_display">${userShortName}</div>
        <div class="user_profile_details | flex_display">
          <div class="user_profile_details_name">${user.first_name} ${user.last_name}</div>
          <div class="user_profile_details_role">Full Stack Engineer</div>
          <button class="user_profile-details_invite">Invite</button>
        </div>
      </div>
      <div class="user_profile_record | flex_display">
        <div class="user_profile_record_techStacks | flex_display | user_profile_listHeadSection">
          <h5>Tech Stack</h5>
          <ul class="user_profile_techStack | flex_display">${techStacks}</ul>
        </div>
        <div class="user_profile_record_projects | flex_display | user_profile_listHeadSection">
          <h5>Projects</h5>
          <ul class="user_profile_project | flex_display">${gitHubProjects}</ul>
        </div>
      </div>
    `;

    userDisplaySection.appendChild(userDiv);
    addEventListenerToInvite(userDiv);
  });
}

function addEventListenerToInvite(userDiv) {
  userDiv.querySelector('.user_profile-details_invite').addEventListener('click', async () => {
    const projectId = getSelectedProjectId();
    const userId = userDiv.getAttribute('user_id');
    const success = await inviteUserToProject(userId, projectId);

    if (success) {
      userDiv.remove();
      alert('User successfully invited.');
    } else {
      alert('Failed to invite user.');
    }
  });
}

async function inviteUserToProject(userId, projectId) {
  try {
    const response = await fetch(`/user/invite/${userId}/${projectId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      return false;
    }else{
      return true;
    }
  } catch (error) {
    console.error('Fetch error:', error);
    return false;
  }
}

async function enableUserInviteGird(){
  document.querySelector('.user_inivite_section').style.display = 'grid';
  const data = await getProjects();
  projectsSelectionInInviteBar(Object.values(data));
  addEventListenerToIniviteButton();
}

function projectsSelectionInInviteBar(projects){
  const projselectionBar = document.querySelector('#proj_selection');

  let projectOptions = '';

  projects.forEach( project => {

    projectOptions += `
      <option value="${project.projName}"
        projId="${project.projId}">${project.projName}
      </option>`;

  });

  projselectionBar.innerHTML = projectOptions;
}