<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <title>Login</title>
</head>
<body>
    <section class="vh-100" style="background-color: whitesmoke;">
      <% if (request.getAttribute("error") == "true") { %>
         <div class="alert alert-danger alert-dismissible fade show" role="alert" id="warningSection">
             <section>Some error has occurred, Try again!</section>
             <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
         </div>
      <% } %>
        <div class="container py-5 h-100">
          <div class="row d-flex justify-content-center align-items-center h-100">
            <div class="col-12 col-md-8 col-lg-6 col-xl-5">
              <div class="card shadow-2-strong" style="border-radius: 1rem;">
                <div class="card-body p-5 text-center">

                  <h3 class="mb-5">Sign in</h3>
                  <form onsubmit="return encryptPassword()" action="http://localhost:8080/Bank/login" method="post">
                  <div class="form-outline mb-4">
                    <input type="email" id="email" class="form-control form-control-lg" name="email" placeholder="E-mail ID" required/>
                  </div>

                  <div class="form-outline mb-4">
                    <input type="password" id="pass" class="form-control form-control-lg" name="password" placeholder="Password" required/>
                  </div>
                  <button class="btn btn-dark btn-lg btn-block" type="submit">Login</button>
                  <p class="small fw-bold mt-2 pt-1 mb-0">Don't have an account? <a href="/Bank/register"
                    class="link-primary">Register</a></p>
                    </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
      <script>
        function encryptPassword(){
          try {
            var passwordElement = document.getElementById('pass');
            let password = passwordElement.value;
            let encodedData = window.btoa(password);
            passwordElement.value = encodedData;
            return true;
          } catch (error) {
            console.log(error.message);
          }

        }
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>
</html>