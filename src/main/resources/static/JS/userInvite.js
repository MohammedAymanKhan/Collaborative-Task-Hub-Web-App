addEventOnInviteContributor();

async function searchUser(userDetails){
  fetch(`/searchContributor/${userDetails}`,
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
    }).then(data => {
      console.log(data);
    }).catch(error => {
      console.error(error);
    });
}


function addEventOnInviteContributor(){
  document.querySelector('.invit_people .fa-magnifying-glass')
          .addEventListener('click',()=>{
            let userDetails=document.querySelector('.invit_people #userDetails').value;
            alert(userDetails);
            searchUser(userDetails);
          });
}
