<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "https://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
        <title>Error Page</title>
    </head>
    <body>
    <% if(response.getStatus() == 500){ %>
        <h1 class="fs-1 text-danger">Error: <%=exception.getMessage() %></h1>
        <p class="fs-3">Please go to <a class="link-light" href="http://localhost:8080/Bank/?customersOp=0">Home page</a><p>
    <%}else {%>
        <p class="fs-1">Hi There, error code is <%=response.getStatus() %><p>
        <p class="fs-3">Please go to <a class="link-light" href="http://localhost:8080/Bank/?customersOp=0">Home page</a><p>
    <%} %>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    </body>
</html>