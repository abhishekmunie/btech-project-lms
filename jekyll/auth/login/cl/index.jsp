---
layout: auth
title: Chief Librarian Login Required
jsp_imports:
  java.sql.*: page
  com.abhishekmunie.librarymanagementsystem.cl.ChiefLibrarian: page
---

<%
  try {
    if (ChiefLibrarian.isChiefLibrarian(user.getEmail())) {
      response.sendRedirect("/cl/");;
    }
  } catch (SQLException e) {
    e.printStackTrace();
  }
%>
<div class="jumbotron">
  <h1>Chief Librarian Portal <small>Chief Librarian Login Required</small></h1>
  <p class="lead">You must be logged in as Chief Librarian to access this area.</p>
  <p><a class="btn btn-lg btn-success" href="<%=userService.createLogoutURL(userService.createLoginURL(request.getRequestURI().replaceAll("/index.jsp", "/")))%>" role="button">Loggin as Chief Librarian</a></p>
</div>
