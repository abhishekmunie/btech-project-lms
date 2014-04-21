---
layout: cl
title: Staff Management
jsp_imports:
  java.io.IOException: page
  java.util.*: page
  com.abhishekmunie.librarymanagementsystem.staff.Staff: page
---

<%
  if (request.getAttribute("isForwordedRequest") == null) {
    request.getRequestDispatcher("/cl/staff-management/").forward(request, response);
    return;
  }
%>

<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">Staff Members</h3>
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
      <p><%=ex.getLocalizedMessage()%></p>
    </details>
  </div>
  <% } %>
  <div class="panel-body">
  <%
    Vector<Staff> staffs = Staff.getAllStaff();
    if (staffs.size() > 0) {
  %>
    <ul class="list-group">
    <%  for (Staff staff : staffs) { %>
      <li class="list-group-item">
        <%=staff.getEmailid()%>
        <form action="/cl/staff-management/" method="post" class="form-inline pull-right" role="form">
          <input type="hidden" name="emailid" value="<%=staff.getEmailid()%>" />
          <input type="hidden" name="action" value="remove" />
          <button type="submit" class="btn-link" ><i class="fa fa-ban text-danger"></i></button>
        </form>
      </li>
    <%  } %>
    </ul>
  <% } else { %>

  <% } %>
  </div>
  <div class="panel-footer">
    <form action="/cl/staff-management/" method="post" class="form-horizontal" role="form">
      <input type="hidden" name="action" value="add" />
      <div class="input-group">
        <label for="inputEmail" class="input-group-addon hidden-xs control-label" >Staff Email address</label>
        <input type="email" class="form-control" id="inputEmail"  {% set_attribute_named_when_autofill emailid%} placeholder="Email address of Staff Member you want to add. e.g.: staff@gmail.com" />
        <span class="input-group-btn">
          <input type="submit" class="btn btn-primary col-sx-12" value="Add" />
        </span>
      </div>
    </form>
  </div>
</div>
