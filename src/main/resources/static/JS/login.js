

const loginBtn = document.querySelector('#userSumbit');

loginBtn.addEventListener('click', async () => {
  const name = document.querySelector('input[name="name"]').value;
  const email = document.querySelector('input[name="email"]').value;
  const password = document.querySelector('input[name="password"]').value;

  const user = { name, email, password };

  try {
    const response = await fetch('/user/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(user)
    });

    if (!response.ok) {
      throw new Error(`Network response was not ok (status: ${response.status})`);
    }else{
      const userData = await response.json();
      alert(userData);
    }

  } catch (error) {
    console.error('Error:', error);
    alert('Login failed. Please check your credentials.');
  }
});


//loginBtn = document.querySelector('#userSumbit');
//
//loginBtn.addEventListener('click', ()=>{
//	let name = document.querySelector('input[name="name"]').value;
//    let email = document.querySelector('input[name="email"]').value;
//    let password = document.querySelector('input[name="password"]').value;
//
//
//    let user = {'name':name,
//                'email': email,
//                'password': password
//              };
//    loginUser(user);
//});
//
////working 1st
//function loginUser(user) {
//
//    fetch('/user/login',
//    {
//        method: 'POST',
//        headers: {
//        'Content-Type': 'application/json'
//    	},
//    	body: JSON.stringify(user)
//    }).then(response => {
//        if (response.ok) {
//            return response.json();
//        }else{
//            throw new Error('Network response was not ok');
//        }
//    }).then(userData => {
//       alert(userData);
//    }).catch(error => {
//        console.error('Fetch error:', error);
//    });
//
//}






































