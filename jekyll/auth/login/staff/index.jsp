---
layout: auth
title: Staff Login Required
jsp_imports:
  java.sql.*: page
  com.abhishekmunie.librarymanagementsystem.staff.Staff: page
---

<%
  try {
    if (Staff.isStaff(user.getEmail())) {
      response.sendRedirect("/staff/");;
    }
  } catch (SQLException e) {
    e.printStackTrace();
  }
%>
<div class="jumbotron">
  <h1>Staff Portal <small>Staff Login Required</small></h1>
  <p class="lead">You must be logged in as a staff to access this area.</p>
  <p><a class="btn btn-lg btn-success" href="<%=userService.createLogoutURL(userService.createLoginURL(request.getRequestURI().replaceAll("/index.jsp", "/")))%>" role="button">Loggin as Staff</a></p>
</div>
