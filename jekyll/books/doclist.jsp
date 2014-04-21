---
layout: books
title: All Book Documents
jsp_imports:
  java.io.IOException: page
  java.util.*: page
  java.sql.*: page
  com.abhishekmunie.librarymanagementsystem.books.Search: page
  com.google.appengine.api.search.Document: page
---

<ul>
<%
  List<Document> documents = Search.getAllBookDocuments();
  for (Document document : documents) {
%>
  <li><%=document.getId()%></li>
<% } %>
</ul>
