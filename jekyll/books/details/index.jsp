---
layout: books
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
{% customtitle %}

<%
  AvailableBook detailsBook = null;
  String urlComponents[] = request.getRequestURI().split("/");
  String detailsISBN = urlComponents[3];
  if (Utilities.isValidISBN(detailsISBN)) {
    detailsBook = AvailableBook.getAvailableBookForISBN(detailsISBN);
%>
<%=detailsBook.getTitle()%> &middot; Details
{% endcustomtitle %}
    <div id="book-details" class="container">
      <div class="page-header">
        <h1><i class="fa fa-book"></i> <%=detailsBook.getTitle()%>
          <small>
            <% Author detailsAuthors[] = detailsBook.getAuthors();
              if(detailsAuthors.length > 0) {
            %>
            by
            <%
                for (int i = 0; i < detailsAuthors.length; i++) {
            %><% if (i!=0) { %>, <% } %>
            <a href="/search?authors=<%=detailsAuthors[i].getName()%>"><%=detailsAuthors[i].getName()%></a><%
                }
              }
            %>
          </small>
        </h1>
      </div>
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
        <li>PublicationYear: <a href="/search?publicationYear=<%=detailsBook.getPublicationYear()%>"><%=detailsBook.getPublicationYear()%></a></li>
        <li>Categories:
          <% Category detailsCategories[] = detailsBook.getCategories();
             for (int i = 0; i < detailsCategories.length; i++) { %>
            <% if (i!=0) { %>, <% } %><a href="/search?categories=<%=detailsCategories[i].getName()%>"><%=detailsCategories[i].getName()%></a>
          <% } %>
        </li>
        <li>Edition: <a href="/search?edition=<%=detailsBook.getEdition()%>"><%=detailsBook.getEdition()%></a></li>
        <% if (detailsBook.getNoOfPages() > 0) { %>
        <li>No of Pages: <a href="/search?noOfPages=<%=detailsBook.getNoOfPages()%>"><%=detailsBook.getNoOfPages()%></a></li>
        <% } %>
        <li>Total Copies: <%=detailsBook.getTotalCopies()%></li>
        <li>Available Copies: <%=detailsBook.getAvailableCopies()%></li>
        <li>Call no.: <%=detailsBook.getCallNo()%></li>
      </ul>
      <%if (detailsVolume != null) { %>
      <a class="btn btn-default" href="<%=detailsInfo.getInfoLink()%>" role="button">
        <span class="glyphicon glyphicon-book"></span>
        View on Google Books
      </a>
      <% } %>
      <span class="clearfix"></span>
      <h3>Description</h3>
      <%=detailsBook.getDescription()%>
    </div>
<%
  } else {
    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request");
    return;
  }
%>