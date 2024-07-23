
const msgBox=document.querySelector('.msgBox');
const msgText=document.querySelector('#sendMessagesInput');

//Forwords messages to chat Group
function sendContributerMessages(){

  let msgId = msgBox.lastElementChild?.getAttribute('msgId');
  msgId = msgId != null ? (Number(msgId)+1).toString() : 0;
  const message={
    'msgId': msgId,
    'messageText':msgText.innerText
  }
  msgText.innerText='';

  sendMessage('/gotMsg',message);
}

// Dispatch Messages to SendByMe and SendByOthers
function displayProjectsMessages(messageArray){

  messageArray.forEach( (message) => {
    if(message.senderName === 'me'){
      displayMessageSendByMe(message);
    }else{
      displayMessage(message);
    }
  });

  scrollToBottomChat();

}

// To Display Messages Send by Me(User)
function displayMessageSendByMe(message){

  let msgElement = getMyMsgElement(message);

  msgElement.querySelector('.msg_upDel .delete').addEventListener('click',()=>{
    sendMessage('/deleteMsg',Number(msgElement.getAttribute('msgId')));
  });

  msgElement.addEventListener('click',()=>{
    updateConvertMsgEleToInputEle(msgElement.querySelector('span'),Number(msgElement.getAttribute('msgId')));
  });

  msgBox.appendChild(msgElement);
  scrollToBottomChat();

}
function getMyMsgElement(message){
  let liElement=document.createElement('li');
  liElement.setAttribute('msgId',message.msgId);
  liElement.classList.add("message", "flex_display" , "message_By_Me");
  let userMsg=`
    <div class="msg_upDel flex_display">
      <i class="fa-solid fa-pen"></i>
      <i class="fa-solid fa-trash | delete"></i>
    </div>

    <span class="text-msg">${message.messageText}</span>

    <div class="msg_user | flex_display">you</div>
    `;
  liElement.innerHTML=userMsg;
  return liElement;
}

// To diplay Messages send By others
function displayMessage(messages){
    let msgElement=getMsgElement(messages);
    msgBox.appendChild(msgElement);

}
function getMsgElement(message){
  let liElement=document.createElement('li');
  liElement.setAttribute('msgId',message.msgId);
  liElement.classList.add("message", "flex_display");
  let userShortName='';
  message.senderName.split(' ').forEach(element => {
    userShortName+=element[0].toUpperCase();
  });
  let userMsg=`
    <div class="msg_user | flex_display">${userShortName}</div>
    <span class="text-msg">${message.messageText}</span>
    `;
  liElement.innerHTML=userMsg;
  return liElement;
}

// To Delete Chat Message
function deletedChatMessage(messageId){
  document.querySelector(`[msgId="${messageId}"]`)?.remove();
}

// To Update Message
function updateConvertMsgEleToInputEle(msgSpanElement,msgId){

  //remove hover effect
  msgSpanElement.parentElement.style.pointerEvents = 'none';

  msgSpanElement.contentEditable = true;
  msgSpanElement.id='chatMsgInput';
  msgSpanElement.classList.add('flex_display');

  msgSpanElement.addEventListener('keypress', (e)=> {
    if (e.key === 'Enter') {
      const message={
        'msgId': msgId,
        'messageText': msgSpanElement.innerText,
      }
      sendMessage('/updateMessage',message);
      updatedMsgElementReplaceBack(msgSpanElement);
    }
  });

}

function updatedMsgElementReplaceBack(msgSpanElement){

  msgSpanElement.contentEditable = false;
  msgSpanElement.removeAttribute('id');
  msgSpanElement.classList.remove('flex_display');
  msgSpanElement.parentElement.removeAttribute('style');

}

function updateMsgFromGroup(updatedMessage){

  let messageElement=document.querySelector(`[msgId="${updatedMessage.msgId}"]`);

  messageElement.querySelector('span').innerHTML=updatedMessage.messageText;

}

function scrollToBottomChat() {
  msgBox.scrollTop = msgBox.scrollHeight;
}