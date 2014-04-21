---
layout: staff_issue
title: Issue Book
---

<%
  if (request.getAttribute("isForwordedRequest") == null) {
    request.getRequestDispatcher("/staff/books/issue/").forward(request, response);
    return;
  }
%>

<div class="panel panel-default">
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
  <div class="panel-body">
    <form action="/staff/books/issue/<%=((request.getQueryString() != null) ? "?" + request.getQueryString() : "")%>" method="post" class="form-horizontal" role="form">
      <div class="form-group">
        <label for="inputBookISBN" class="col-sm-2 control-label">Book ISBN</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="inputBookISBN" {% set_parameter_named bookISBN%} placeholder="ISBN" required />
        </div>
      </div>
      <div class="form-group">
        <label for="inputUserId" class="col-sm-2 control-label">User ID</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="inputUserId" {% set_parameter_named userID%} placeholder="User Id" required />
        </div>
      </div>
      <div class="form-group">
        <label for="inputUserEmail" class="col-sm-2 control-label">User Email</label>
        <div class="col-sm-10">
          <input type="email" class="form-control" id="inputUserId" {% set_parameter_named userEmail%} placeholder="User Email" required />
        </div>
      </div>
      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <input type="submit" class="btn btn-primary" value="Issue" />
        </div>
      </div>
    </form>
</div>
