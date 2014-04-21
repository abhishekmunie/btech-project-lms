---
layout: user
title: Currently Issued Books
jsp_imports:
  java.io.IOException: page
  java.util.*: page
  com.abhishekmunie.librarymanagementsystem.books.Issue: page
---

<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">Currently Issued Books</h3>
  </div>
  <div class="panel-body">
    <%
      Vector<Issue> issues = Issue.getCurrentIssuesForUserWithEmail(user.getEmail());
      if (issues.size() > 0) {
    %>
    <table class="table table-striped table-hover table-responsive">
      <thead>
        <tr>
          <th>Issue Id</th>
          <th>Book ISBN</th>
          <th>Issue Date</th>
        </tr>
      </thead>
      <tbody>
        <% for (Issue issue : issues) { %>
        <tr>
          <td><%=issue.getIssueID()%></td>
          <td><%=issue.getBookISBN()%></td>
          <td><%=issue.getIssueDate()%></td>
        </tr>
        <% } %>
      </tbody>
    </table>
    <% } else { %>
      No Issues Found.
    <% } %>
  </div>
</div>
