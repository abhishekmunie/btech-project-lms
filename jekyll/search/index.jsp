---
layout: search
title: Search Books&hellip;
sitemap:
  lastmod: 2014-01-23
  priority: 0.7
  changefreq: monthly
jsp_imports:
  java.sql.*: page
  com.google.appengine.api.utils.SystemProperty: page
  com.abhishekmunie.librarymanagementsystem.CONFIG: page
  com.abhishekmunie.librarymanagementsystem.books.Book: page
  com.abhishekmunie.librarymanagementsystem.books.Search: page
---
<%
   if (request.getQueryString() != null && request.getQueryString().equalsIgnoreCase("q=")) {
     response.sendRedirect(request.getRequestURI().replaceAll("/index.jsp$", "/"));
     return;
   }
%>
<% if (request.getQueryString() == null) { %>
<div class="container">
  <div class="jumbotron">
    <h1>Welcome to {{site.name}}!</h1>
    <p><a href="/about" class="btn btn-primary btn-lg" role="button">Learn more</a></p>

    <form action="<%= request.getRequestURI().replaceAll("/index.jsp$", "/") %>" method="get" role="form">
      <div class="input-group">
        <input type="text" class="form-control" id="inputSearch" name="q" placeholder="Search Books..." />
        <span class="input-group-btn"><button class="btn btn-primary" type="submit"><i class="fa fa-search"></i></button></span>
      </div>
    </form>
  </div>
</div>
<% } else { %>
<div class="container-fluid">
  <div class="row">
    <div class="col-md-8">
      <ul class="nav nav-tabs nav-justified">
      </ul>
      <div class="container-fluid">
        <h3>Results</h3>
        <%
          // String start = "0";
          // String last = "100";
          String q = request.getParameter("q");
          String isbn = request.getParameter("isbn");
          String title = request.getParameter("title");
          String publisher = request.getParameter("publisher");
          String publicationYear = request.getParameter("publicationYear");
          String edition = request.getParameter("edition");
          String description = request.getParameter("description");
          String googleBooksID = request.getParameter("googleBooksID");
          String concatinatedAuthorsString = request.getParameter("authors");
          String concatinatedCategoriesString = request.getParameter("categories");
          Book books[] = Search.getBooksForSearchFields(q, isbn, title, publisher, publicationYear, edition, description, googleBooksID, concatinatedAuthorsString, concatinatedCategoriesString);
          if (books != null) {
        %>
        <div class="list-group">
          <% for (int i = 0; i < books.length; i++) { Book book = books[i]; %>
          <div class="panel panel-default">
              <div class="panel-heading">
                <h3 class="panel-title"><a href="/books/details/<%=book.getISBN()%>"><%=book.getTitle()%></a></h3>
              </div>
              <div class="panel-body">
                <div><%=book.getISBN()%></div>
              </div>
          </div>
          <% } %>
        </div>
        <% } %>
      </div>
    </div>
    <div class="col-md-4">
      <form action="<%= request.getRequestURI().replaceAll("/index.jsp$", "/") %>" method="get" role="form">
        <div class="input-group">
          <span class="input-group-addon"><i class="fa fa-search"></i></span>
          <input type="text" class="form-control" id="inputSearch" placeholder="Search Books..." {% set_parameter_named q%}/>
        </div>
        <h3>Advanced Filters</h3>
        <div class="form-group">
          <label for="inputTitle">Title</label>
          <input type="text" class="form-control" id="inputTitle" placeholder="Title" {% set_parameter_named title%}/>
          <p class="help-block">e.g. Fundamentals of Physics</p>
        </div>
        <div class="form-group">
          <label for="inputISBN">ISBN</label>
          <input type="text" class="form-control" id="inputISBN" placeholder="ISBN" {% set_parameter_named isbn%}/>
        </div>
        <div class="form-group">
          <label for="inputAuthors">Authors</label>
          <input type="text" class="form-control" id="inputAuthors" placeholder="Authors"  {% set_parameter_named authors%}/>
        </div>
        <div class="form-group">
          <label for="inputPublisher">Publisher</label>
          <input type="text" class="form-control" id="inputPublisher" placeholder="Publisher" {% set_parameter_named publisher%}/>
        </div>
        <div class="form-group">
          <label for="inputPublicationYear">Publication Year</label>
          <input type="text" class="form-control" id="inputPublicationYear" placeholder="Publication Year" {% set_parameter_named publicationYear%}/>
        </div>
        <div class="form-group">
          <label for="inputEdition">Edition</label>
          <input type="number" class="form-control" id="inputEdition" placeholder="Edition" min="1" {% set_parameter_named edition%}/>
        </div>
        <div class="form-group">
          <label for="inputCategories">Categories</label>
          <input type="text" class="form-control" id="inputCategories" placeholder="categories" {% set_parameter_named categories%}/>
        </div>
        <div class="form-group">
          <label for="inputDescription">Description</label>
          <input type="text" class="form-control" id="inputDescription" placeholder="description" {% set_parameter_named description%}/>
        </div>
        <div class="form-group">
          <label for="inputGoogleBooksID">Google Books ID</label>
          <div class="input-group">
            <span class="input-group-addon"><span class="glyphicon glyphicon-globe"></span></span>
            <input type="text" class="form-control" id="inputGoogleBooksID" placeholder="Google Books ID" {% set_parameter_named googleBooksID%}/>
          </div>
        </div>
        <input type="submit" class="btn btn-primary" value="Search"/>
      </form>
    </div>
  </div>
</div>
<% } %>
