const registerForm = document.getElementById('registerForm');
registerForm.addEventListener('submit', (event) => {
  event.preventDefault();
  postCustomerData();
});

//This hides the error message while registering
document.getElementsByClassName("alert-close")[0].addEventListener('click', (event) => {
    document.getElementsByClassName("alert-close")[0].parentElement.style.visibility = "hidden";
})

function postCustomerData() {
    const formData = new FormData(registerForm);
    const customerData = { username: formData.get('name'), email: formData.get('email'), password: formData.get('password'), accountNumber: formData.get('accNum')};
    const accountDate = { account_num: formData.get('accNum'), balance: 0.00}
    fetch('http://localhost:8080/Bank/api/account', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(accountDate),
        }).then(response => {
        if (response.status == 200) {
            console.log('Successfully created Account!');
               fetch('http://localhost:8080/Bank/api/customer', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(customerData),
                }).then((response) => {
                    if (response.status == 200) {
                        console.log('Successfully registered!');
                        window.location = "http://localhost:8080/Bank/login";
                    }
                    else if (response.status != 200) {
                        console.log('User not registered!');
                        document.getElementById("warningSection").style.visibility = "visible";
                    }
                });
        }
        else if (response.status != 200) {
            console.log('Account not created!');
            document.getElementById("warningSection").style.visibility = "visible";
        }
    });
}

