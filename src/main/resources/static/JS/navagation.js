let notifElement = null;

async function getNotifications() {
  try {
    const response = await fetch('/user/notification', {
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

document.querySelector('.notifcation_userInivite img').
  addEventListener('click',async ()=>{
    if(notifElement === null){
      notifElement = await userNotificationDisplay();
      document.querySelector('.notifcation_userInivite').append(notifElement);
      addEventListToAccpAndRejButton(notifElement);
    }else{
      notifElement.remove();
      notifElement = null;
    }
  });

// Function to display notifications
async function userNotificationDisplay() {
  let notifElement = document.createElement('div');
  notifElement.classList.add('notifcation_container','flex_display' ,'scroll_bar_style');

  const notificationData = await getNotifications();
  console.log(notificationData);

  let notifications = '';

  notificationData.forEach(notification => {
    notifications += `
      <div class="notification_project" projId="${notification.projId}">
        <div class="notification_projectname">${notification.projName}</div>
        <div class="notification_button | flex_display">
          <button class="accepeted">accepted</button>
          <button class="rejected">reject</button>
        </div>
      </div>
    `;
  });

  notifElement.innerHTML = notifications;

  return notifElement;
}

function removeNotificationDisplay(notifElement){
  document.querySelector('.notifcation_userInivite').remove(notifElement);
}

function addEventListToAccpAndRejButton(notifElement){

  // Adding event listeners to buttons
  const acceptedButtons = notifElement.querySelectorAll('.accepeted');
  const rejectedButtons = notifElement.querySelectorAll('.rejected');

  acceptedButtons.forEach(button => {
    button.addEventListener('click', () => {
      handleAccepted(button);
    });
  });

  rejectedButtons.forEach(button => {
    button.addEventListener('click', () => {
      handleRejected(button);
    });
  });

}

// Function to call backend and handle response
async function updateNotificationStatus(projId, status) {
  try {
    const response = await fetch(`/user/updateInviteStatus/${projId}/${status}`, {
      method: 'PATCH',
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

// Event handler for accepted button
async function handleAccepted(button) {
  const projectElement = button.closest('.notification_project');
  const projectId = projectElement.getAttribute('projId');
  const projectName = projectElement.querySelector('.notification_projectname').textContent;
  console.log('Accepted project ID:', projectId);
  console.log('Accepted project name:', projectName);

  // Call backend to update status
  const isUpdated = await updateNotificationStatus(projectId, true);

  if (isUpdated) {
    projectElement.remove();
    displayProjects([{ projId: projectId, projName: projectName }]);
  } else {
    console.error('Failed to update project status');
  }
}

// Event handler for rejected button
async function handleRejected(button) {
  const projectId = button.closest('.notification_project').getAttribute('projId');
  console.log('Rejected project ID:', projectId);

  const result = await updateNotificationStatus(projectId, false);

  if (result) {
    button.closest('.notification_project').remove();
  } else {
    console.error('Failed to update project status');
  }
}