---
layout: staff_issue
title: Return Book
jsp_imports:
  java.io.IOException: page
  java.util.*: page
  com.abhishekmunie.librarymanagementsystem.books.Issue: page
---

<%
  if (request.getAttribute("isForwordedRequest") == null) {
    request.getRequestDispatcher("/staff/books/return/").forward(request, response);
    return;
  }
  String emailID = request.getParameter("emailID");
  if (emailID != null && emailID.trim().equals("")) {
    response.sendRedirect(request.getRequestURI().replaceAll("/index.jsp", "/"));
    return;
  }
%>

<div class="panel panel-default">
  <div class="panel-heading">
    <form action="/staff/books/return/" method="get" class="form-horizontal" role="form">
      <div class="input-group">
        <label for="inputEmail" class="input-group-addon hidden-xs control-label" >User Email address</label>
        <input type="email" class="form-control" id="inputEmail"  {% set_parameter_named emailID%} placeholder="Email address of User. e.g.: user@gmail.com" />
        <span class="input-group-btn">
          <input type="submit" class="btn btn-primary col-sx-12" value="Get Issues" />
        </span>
      </div>
    </form>
  </div>
  <%
    String successmsg = (String)request.getAttribute("successmsg");
    String errormsg = (String)request.getAttribute("errormsg");
    Exception ex = (Exception)(request.getAttribute("errorex"));
    if (successmsg != null) {
  %>
  <div class="alert alert-success alert-dismissable">
    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
    <strong>Success!</strong> <%= (successmsg.equals("")) ? "The book was successfully added." : successmsg %>
  </div>
  <% } else if (errormsg != null) { %>
  <div class="alert alert-danger alert-dismissable">
    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
    <details>
      <summary><strong>Error!</strong> <%=errormsg%></summary>
      <p><%=(ex != null ) ? ex.getLocalizedMessage() : "" %></p>
    </details>
  </div>
  <% } %>
  <% if (emailID != null) { %>
  <div class="panel-body">
    <%
      Vector<Issue> issues = Issue.getCurrentIssuesForUserWithEmail(emailID);
      if (issues.size() > 0) {
    %>
    <table class="table table-striped table-hover table-responsive">
      <thead>
        <tr>
          <th>Issue Id</th>
          <th>User Id</th>
          <th>User Email</th>
          <th>Issue Date</th>
          <th> </th>
        </tr>
      </thead>
      <tbody>
        <% for (Issue issue : issues) { %>
        <tr>
          <td><%=issue.getIssueID()%></td>
          <td><%=issue.getUserID()%></td>
          <td><%=issue.getUserEmail()%></td>
          <td><%=issue.getIssueDate()%></td>
          <td>
            <form action="/staff/books/return/" method="post" class="form-inline" role="form">
              <input type="hidden" name="issueID" value="<%=issue.getIssueID()%>" />
              <input type="hidden" name="emailID" value="<%=emailID%>" />
              <input typr="submit" class="btn btn-default" value="Return" />
            </form>
          </td>
        </tr>
        <% } %>
      </tbody>
    </table>
    <% } else { %>
      No Issues Found.
    <% } %>
  </div>
  <% } %>
</div>
