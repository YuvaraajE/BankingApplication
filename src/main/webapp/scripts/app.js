const registerForm = document.getElementById('registerForm');
registerForm.addEventListener('submit', (event) => {
  event.preventDefault();
  postCustomerData();
});

function postCustomerData() {
    const formData = new FormData(registerForm);
    const data = { username: formData.get('name'), email: formData.get('email'), password: formData.get('password'), accountNumber: formData.get('accNum')};
    fetch('http://localhost:8080/Bank/register', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
    }).then((response) => {
        if (response.status == 200) {
            console.log('Successfully registered!');
        }
    });
}

