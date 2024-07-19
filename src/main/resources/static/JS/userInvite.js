
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
//  let userByUrl = searchDetails.length > 1 ? searchDetails[1] : searchDetails[0];
    console.log(searchByUrl);
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


addEventListenerToIniviteButton();


function displayUsersSearchList(usersList){


  usersList.forEach(user => {

    const userDiv = document.createElement('div');

    userDiv.className = 'user_profile_section';
    userDiv.setAttribute('user_id', user.user_id);

    let userShortName = user.first_name[0].toUpperCase()+
            user.last_name[0].toUpperCase();

    userDiv.innerHTML = `
      <div class="user_profile | flex_display">

        <div class="user_profile_cricle | flex_display">${userShortName}</div>
        
        <div class="user_profile_details | flex_display">

          <div class="user_profile_details_name">${user.first_name+" "+user.last_name}</div>
          <div class="user_profile_details_role">Full Stack Engineer</div>
          <button class="user_profile-details_invite">Invite</button>

        </div>

      </div>

      <div class="user_profile_record | flex_display">

        <div class="user_profile_record_techStacks | flex_display | user_profile_listHeadSection">

          <h5>Tech Stack</h5>

          <ul class="user_profile_techStack | flex_display">

            ${user.userDetails?.techStacks?.map(stack => `<li>${stack}</li>`).join('')}

          </ul>

        </div>

        <div class="user_profile_record_projects | flex_display | user_profile_listHeadSection">

          <h5>Projects</h5>

          <ul class="user_profile_project | flex_display">

            ${Object.entries(user.userDetails?.gitHubProjects).map(([project, url]) =>
              `<li><a href="${url}">${project}</a></li>`).join('')}

          </ul>

        </div>

      </div>`;

    document.querySelector('.user_display_section').appendChild(userDiv);

  });  

}