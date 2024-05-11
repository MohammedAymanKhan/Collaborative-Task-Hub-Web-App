//contributer dynamic display
function contributerDisplay(contributerPeople){
  const contList=document.querySelector('.contributor>ul');
  contributerPeople.forEach(contributer=>{
    const liElement=document.createElement('li');
    liElement.classList.add("flex_display");
    liElement.innerHTML=`<div class="user_circle | orange_circle"></div> <span>${contributer.assign}</span>`;
    contList.append(liElement);
  });
}