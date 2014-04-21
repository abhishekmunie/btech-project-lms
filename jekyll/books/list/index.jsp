---
layout: books
title: All Books
jsp_imports:
  java.io.IOException: page
  java.util.*: page
  java.sql.*: page
  com.google.api.services.books.model.Volume: page
  com.google.api.services.books.model.Volume.VolumeInfo: page
  com.abhishekmunie.librarymanagementsystem.books.GoogleBooks: page
  com.abhishekmunie.librarymanagementsystem.books.AvailableBook: page
  com.abhishekmunie.librarymanagementsystem.books.Author: page
  com.abhishekmunie.librarymanagementsystem.books.Category: page
---
<%
   if (request.getQueryString() != null && request.getQueryString().equalsIgnoreCase("details=")) {
     response.sendRedirect(request.getRequestURI().replaceAll("/index.jsp", "/"));
     return;
   }
%>
<div id="books-list" class="container-fluid">
  <div class="row">
    <div class="col-md-8">
      <div class="container-fluid">
        <%
          String detailsISBN = request.getParameter("details");
          long total = 0;
          try {
            total = AvailableBook.getTotalCount();
          } catch (SQLException e) {
            e.printStackTrace();
          }

          long current = 0;
          try {
            String currentPage = request.getParameter("currentPage");
            current = Long.parseLong(currentPage);
          } catch (NumberFormatException E) {
          }
          long length = 10;
          try {
            String resultsPerPage = request.getParameter("resultsPerPage");
            length = Long.parseLong(resultsPerPage);
          } catch (NumberFormatException E) {
          }
          long start = current * length;
          long totalPages = (long)Math.ceil((float)total / length);
          if (current < 0 || (totalPages != 0 && current >= totalPages)) {
            response.sendRedirect(request.getRequestURI().replaceAll("/index.jsp", "/"));
            return;
          }
          AvailableBook detailsBook = null;
          try {
            Vector<AvailableBook> availableBooks = AvailableBook.getAllAvailableBooksWithAuthorsAndCatagoriesInRange(start, length);
            if (request.getParameter("autoSelectFirst") != null) {
              response.sendRedirect("?details="+availableBooks.elementAt(0).getISBN()+"&currentPage="+current+"&resultsPerPage="+length);
            }
        %>
        <div class="text-center">
          <ul class="pagination">
            <li <%= (current > 0) ? "><a href=\"?currentPage="+(current-1)+"&amp;resultsPerPage="+length+((detailsISBN!=null)?"&autoSelectFirst":"")+"\">&laquo;</a" : "class=\"disabled\"><span>&laquo;</span"%>></li>
            <%
              for (int i = 0; i < totalPages; i++) {
                if (i == current) {
            %>
            <li class="active"><span> <%=i+1%> <span class="sr-only">(current) </span></span></li>
            <%  } else { %>
            <li><a href="?currentPage=<%=""+i+"&amp;resultsPerPage="+length+((detailsISBN!=null)?"&autoSelectFirst":"")+"\"> "+(i+1)%> </a></li>
            <%
                }
              }
            %>
            <li <%= (current < totalPages-1) ? "><a href=\"?currentPage="+(current+1)+"&amp;resultsPerPage="+length+((detailsISBN!=null)?"&autoSelectFirst":"")+"\">&raquo;</a" : "class=\"disabled\"><span>&raquo;</span"%>></li>
          </ul>
        </div>
        <table class="table table-striped table-hover table-responsive">
          <thead>
            <tr>
              <th>Title</th>
              <th>Authors</th>
              <th>Publisher</th>
              <th>Categories</th>
              <th> </th>
            </tr>
          </thead>
          <tbody>
            <%
              for (AvailableBook availableBook : availableBooks) {
              	if (availableBook.getISBN().equals(detailsISBN)) {
              		detailsBook = availableBook;
            %>
            <tr class="info"><%  } else { %><tr><%  } %>
              <td><a href="/books/details/<%=availableBook.getISBN()%>"><%=availableBook.getTitle()%></a></td>
              <td>
              <% Author authors[] = availableBook.getAuthors();
                 for (int i = 0; i < authors.length; i++) { %>
                <% if (i!=0) { %>, <% } %><a href="/search?authors=<%=authors[i].getName()%>"><%=authors[i].getName()%></a>
              <% } %>
              </td>
              <td><a href="/search?publisher=<%=availableBook.getPublisher()%>"><%=availableBook.getPublisher()%></a></td>
              <td>
              <% Category categories[] = availableBook.getCategories();
                 for (int i = 0; i < categories.length; i++) { %>
                <% if (i!=0) { %>, <% } %><a href="/search?categories=<%=categories[i].getName()%>"><%=categories[i].getName()%></a>
              <% } %>
              </td>
              <td><a href="?details=<%=availableBook.getISBN()+"&amp;currentPage="+current+"&amp;resultsPerPage="+length%>"><i class="fa fa-chevron-circle-right fa-2x"></i></a></td>
            </tr>
            <%} %>
          </tbody>
        </table>
        <div class="text-center">
          <ul class="pagination">
            <li <%= (current > 0) ? "><a href=\"?currentPage="+(current-1)+"&amp;resultsPerPage="+length+((detailsISBN!=null)?"&autoSelectFirst":"")+"\">&laquo;</a" : "class=\"disabled\"><span>&laquo;</span"%>></li>
            <%
              for (int i = 0; i < totalPages; i++) {
                if (i == current) {
            %>
            <li class="active"><span> <%=i+1%> <span class="sr-only">(current) </span></span></li>
            <%  } else { %>
            <li><a href="?currentPage=<%=""+i+"&amp;resultsPerPage="+length+((detailsISBN!=null)?"&autoSelectFirst":"")+"\"> "+(i+1)%> </a></li>
            <%
                }
              }
            %>
            <li <%= (current < totalPages-1) ? "><a href=\"?currentPage="+(current+1)+"&amp;resultsPerPage="+length+((detailsISBN!=null)?"&autoSelectFirst":"")+"\">&raquo;</a" : "class=\"disabled\"><span>&raquo;</span"%>></li>
          </ul>
        </div>
        <%
          } catch (SQLException e) {
            e.printStackTrace();
          }
        %>
      </div>
    </div>
    <div id="book-details" class="col-md-4">
      <h2><i class="fa fa-book"></i> Details</h2>
      <%
        if (detailsISBN != null) {
          try {
          	if (detailsBook == null) {
          	  detailsBook = AvailableBook.getAvailableBookForISBN(detailsISBN);
          	}
            if (detailsBook != null) {
      %>
      <div class="panel panel-default">
        <div class="panel-heading">
          <h3 class="panel-title"><a href="/books/details/<%=detailsISBN%>"><%=detailsBook.getTitle()%></a></h3>
          <% Author detailsAuthors[] = detailsBook.getAuthors();
             if(detailsAuthors.length > 0) {
          %>
          by
          <% }
             for (int i = 0; i < detailsAuthors.length; i++) { %>
          <% if (i!=0) { %>, <% } %><a href="/search?authors=<%=detailsAuthors[i].getName()%>"><%=detailsAuthors[i].getName()%></a>
          <% } %>
        </div>
        <div class="panel-body">
          <%
            Volume detailsVolume = null;
            VolumeInfo detailsInfo = null;
            if (detailsBook.getGoogleBooksID() != null) {
              try {
                detailsVolume = GoogleBooks.getGoogleBookVolumeByID(detailsBook.getGoogleBooksID(), request);
                detailsInfo = detailsVolume.getVolumeInfo();
          %>
          <img src="<%=detailsInfo.getImageLinks().getThumbnail()%>" class="pull-left img-thumbnail" alt="<%=detailsInfo.getTitle()%> Thumbnail" title="<%=detailsBook.getTitle()%>"/>
          <%
              } catch (IOException e) {}
            }
          %>
          <ul class="list-unstyled">
            <li>ISBN: <a href="/books/details/<%=detailsISBN%>"><strong><cite><%=detailsBook.getISBN()%></cite></strong></a></li>
            <li>Publisher: <a href="/search?publisher=<%=detailsBook.getPublisher()%>"><%=detailsBook.getPublisher()%></a></li>
            <li>Categories:
              <% Category detailsCategories[] = detailsBook.getCategories();
                 for (int i = 0; i < detailsCategories.length; i++) { %>
                <% if (i!=0) { %>, <% } %><a href="/search?categories=<%=detailsCategories[i].getName()%>"><%=detailsCategories[i].getName()%></a>
              <% } %>
            </li>
            <% if (detailsBook.getNoOfPages() > 0) { %>
            <li>No of Pages: <a href="/search?noOfPages=<%=detailsBook.getNoOfPages()%>"><%=detailsBook.getNoOfPages()%></a></li>
            <% } %>
            <li>Available Copies: <%=detailsBook.getAvailableCopies()%></li>
            <li>Call no.: <%=detailsBook.getCallNo()%></li>
          </ul>
          <%if (detailsVolume != null) { %>
          <a class="btn btn-default" href="<%=detailsInfo.getInfoLink()%>" role="button">
            <span class="glyphicon glyphicon-book"></span>
            View on Google Books
          </a>
          <% } %>
        </div>
        <div class="panel-footer">
          <%=detailsBook.getDescription()%>
        </div>
      </div>
      <%
            }
          } catch (SQLException e) {
            e.printStackTrace();
          }
        } else {
      %>
      The libray has <%=""+total%> books.
      <%}%>
    </div>
  </div>
</div>
