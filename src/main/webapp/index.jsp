<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.bank.BeanClass.Customer, com.bank.BeanClass.Account, com.bank.BeanClass.Transaction, java.util.ArrayList"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <title>Bank - Home</title>
</head>
<body>
  <div class="container-fluid p-0">
  <!------------------------------------------------ Edit Customer Modal ------------------------------------>
  <div class="modal fade" id="editCustomer" tabindex="-1" aria-labelledby="editCustomerLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="editCustomerLabel">Edit Customer</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <form id="editForm" action="http://localhost:8080/Bank/customerEdit" method="POST">
        <div class="modal-body">
            <input type="text" class="form-control" id="cust_id" name="cust_id" placeholder="Customer ID" value="<%= ((com.bank.BeanClass.Customer)session.getAttribute("customer")).getId() %>" hidden>
            <div class="mb-3">
              <label for="name" class="col-form-label">Name:</label>
              <input type="text" class="form-control" id="name" name="name" placeholder="Name" value="<%= ((com.bank.BeanClass.Customer)session.getAttribute("customer")).getName() %>">
            </div>
            <div class="mb-3">
              <label for="accountNumber" class="col-form-label">Account Number:</label>
              <input type="text" class="form-control" id="accountNumber" name="accountNumber" value="<%= ((com.bank.BeanClass.Customer)session.getAttribute("customer")).getAccountNumber() %>" readonly>
            </div>
            <div class="mb-3">
              <label for="email" class="col-form-label">E-mail:</label>
              <input type="email" class="form-control" id="email" name="email" value="<%= ((com.bank.BeanClass.Customer)session.getAttribute("customer")).getEmail() %>" readonly>
            </div>
            <div class="mb-3">
              <label for="password" class="col-form-label">Password:</label>
              <input type="password" class="form-control" id="password" name="password" placeholder="Password" value="<%= ((com.bank.BeanClass.Customer)session.getAttribute("customer")).getPassword() %>">
            </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-dark" data-bs-dismiss="modal">Close</button>
          <button type="submit" class="btn btn-outline-danger">Edit</button>
        </div>
    </form>
      </div>
    </div>
  </div>
  <nav class="navbar navbar-dark bg-dark">
      <a class="navbar-brand fs-3 ms-2">Banking Application</a>
      <form class="d-flex">
        <a class="nav-link link-light my-1 me-2" data-bs-toggle="modal" data-bs-target="#editCustomer" type="button">Edit Profile</a>
        <a href="http://localhost:8080/Bank/logout" class="btn btn-outline-danger me-3 my-1">Logout</a>
      </form>
    </nav>
      <% if (request.getAttribute("error") == "true") { %>
         <div class="alert alert-danger alert-dismissible fade show" role="alert" id="warningSection">
             <section>Login was unsuccessful, check your email/password!</section>
             <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
         </div>
      <% } %>
    <div class="m-5">
      <div class="d-flex justify-content-between">
        <h3 class="fs-3">Hi, <%= ((com.bank.BeanClass.Customer)session.getAttribute("customer")).getName() %> </h3>
        <h3 class="fs-3">Current Balance: &#x20b9;<%= ((com.bank.BeanClass.Account)session.getAttribute("account")).getBalance() %> </h3>
      </div>
      <div class="m-5">
        <button type="button" data-bs-toggle="modal" data-bs-target="#deposit"  class="btn btn-outline-dark m-2">Deposit</button>
        <button type="button" data-bs-toggle="modal" data-bs-target="#withdraw" class="btn btn-outline-dark m-2">Withdraw</button>
        <button type="button" data-bs-toggle="modal" data-bs-target="#transfer" class="btn btn-outline-dark m-2">Amount Transfer</button>

        <!------------------------------------------------ Deposit Modal ------------------------------------>
        <div class="modal fade" id="deposit" tabindex="-1" aria-labelledby="depositLabel" aria-hidden="true">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="depositLabel">Deposit Money</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
              </div>
              <div class="modal-body">
                <form action="http://localhost:8080/Bank/transaction/deposit" method="post">
                  <div class="mb-3">
                    <label for="depositMoney" class="col-form-label">Deposit Money:</label>
                    <input type="text" class="form-control" id="depositMoney" name="depositMoney" placeholder="Deposit Amount">
                  </div>
                  <div class="mb-3">
                    <label for="description" class="col-form-label">Description:</label>
                    <input type="text" class="form-control" id="de((com.bank.BeanClass.Customer)session.getAttribute("customer"))scription" name="description" placeholder="Short Description...">
                  </div>
                  <div class="mb-3">
                    <label for="password" class="col-form-label">Password:</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="Password">
                  </div>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-dark" data-bs-dismiss="modal">Close</button>
                <button type="submit" class="btn btn-outline-danger">Deposit</button>
              </div>
            </form>
            </div>
          </div>
        </div>

        <!------------------------------------------------ Withdraw Modal ------------------------------------>
        <div class="modal fade" id="withdraw" tabindex="-1" aria-labelledby="withdrawLabel" aria-hidden="true">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="withdrawLabel">Withdraw Money</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
              </div>
              <div class="modal-body">
                <form action="" method="post">
                  <div class="mb-3">
                    <label for="withdrawMoney" class="col-form-label">Withdraw Money:</label>
                    <input type="text" class="form-control" id="withdrawMoney" name="withdrawMoney" placeholder="Withdraw Amount">
                  </div>
                  <div class="mb-3">
                    <label for="description" class="col-form-label">Description:</label>
                    <input type="text" class="form-control" id="description" name="description" placeholder="Short Description...">
                  </div>
                  <div class="mb-3">
                    <label for="password" class="col-form-label">Password:</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="Password">
                  </div>
                </form>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-dark" data-bs-dismiss="modal">Close</button>
                <button type="button" class="btn btn-outline-danger">Withdraw</button>
              </div>
            </div>
          </div>
        </div>

        <!------------------------------------------------ Transfer Modal ------------------------------------>
        <div class="modal fade" id="transfer" tabindex="-1" aria-labelledby="transferLabel" aria-hidden="true">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="transferLabel">Transfer Money</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
              </div>
              <div class="modal-body">
                <form action="" method="post">
                  <div class="mb-3">
                    <label for="transferMoney" class="col-form-label">Transfer Money:</label>
                    <input type="text" class="form-control" id="transferMoney" name="transferMoney" placeholder="Transfer Amount">
                  </div>
                  <div class="mb-3">
                    <label for="transferAccount" class="col-form-label">Transfer To:</label>
                    <input type="text" class="form-control" id="transferAccount" name="transferAccount" placeholder="Account Number">
                  </div>
                  <div class="mb-3">
                    <label for="description" class="col-form-label">Description:</label>
                    <input type="text" class="form-control" id="description" name="description" placeholder="Short Description...">
                  </div>
                  <div class="mb-3">
                    <label for="password" class="col-form-label">Password:</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="Password">
                  </div>
                </form>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-dark" data-bs-dismiss="modal">Close</button>
                <button type="button" class="btn btn-outline-danger">Transfer</button>
              </div>
            </div>
          </div>
        </div>

      </div>
      <div class="m-5">
      <% if (((ArrayList<Transaction>)session.getAttribute("transactions")).size() == 0) { %>
               <p class="fs-3"> No transaction recorded yet! </p>
        <% } else { %>
        <table class="table table-hover table-striped">
             <tr>
               <th>S.No</th>
               <th>Time</th>
               <th>Description</th>
               <th>Credit</th>
               <th>Debit</th>
               <th>Balance</th>
               <th></th>
             </tr>
             <c:forEach items="${sessionScope.transactions}" var="transaction" varStatus="theCount">
             <tr>
                 <td>${theCount.count}</td>
                 <td>${transaction.getDate()}</td>
                 <td>${transaction.getDescription()}</td>
                 <td>${transaction.getAmtCredit()}</td>
                 <td>${transaction.getAmtDebit()}</td>
                 <td>${transaction.getBalance()}</td>
                 <td> </td>
             </tr>
             </c:forEach>
        </table>
      <% } %>
      </div>
    </div>
  </div>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
  <script src="scripts/app.js"></script>
</body>
</html>