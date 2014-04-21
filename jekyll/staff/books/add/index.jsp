---
layout: staff_books
title: Add new Book
jsp_imports:
  java.util.*: page
  java.security.GeneralSecurityException: page
  java.text.SimpleDateFormat: page
  com.google.api.services.books.model.Volume: page
  com.google.api.services.books.model.Volume.VolumeInfo: page
  com.abhishekmunie.librarymanagementsystem.books.GoogleBooks: page
  com.abhishekmunie.librarymanagementsystem.books.AddServlet: page
  com.google.api.client.util.Joiner: page
---

<%
  if (request.getAttribute("isForwordedRequest") == null) {
    request.getRequestDispatcher("/staff/books/add/").forward(request, response);
    return;
  }

  String autocompleteUsingGoogleBooks = request.getParameter("autocompleteUsingGoogleBooks");
  String googleBooksID = request.getParameter("googleBooksID");
  if (autocompleteUsingGoogleBooks!= null && googleBooksID != null && request.getAttribute("autocompletesUsingGoogleBooksID") == null) {
    if (googleBooksID.equals("")) {
       response.sendRedirect(request.getRequestURI().replaceAll("/index.jsp", "/"));
       return;
    } else {
      Volume volume = null;
      try {
        volume = GoogleBooks.getGoogleBookVolumeByID(googleBooksID, request);
      } catch (Exception e) {
        //response.sendRedirect("/oauth2/googlebooks");
        e.printStackTrace(response.getWriter());
        return;
      }
      VolumeInfo info = volume.getVolumeInfo();
      request.setAttribute("googleBooksID", googleBooksID);
      String isbn = GoogleBooks.getISBNFromVolumeInfo(info);
      if (isbn != null) request.setAttribute("isbn", isbn);
      String title = info.getTitle();
      if (title != null) request.setAttribute("title", title);
      List<String> authors = info.getAuthors();
      if (authors != null) request.setAttribute("authors", Utilities.join(authors));
      String publisher = info.getPublisher();
      if (publisher != null) request.setAttribute("publisher", publisher);
      String publishedDate = info.getPublishedDate();
      if (publishedDate != null) {
        try {
          Calendar cal = Calendar.getInstance();
          cal.setTime((new SimpleDateFormat("yyyy-mm-dd")).parse(publishedDate));
          request.setAttribute("publicationYear", cal.get(Calendar.YEAR));
        } catch (java.text.ParseException e) {
          request.setAttribute("publicationYear", publishedDate);
        }
      }
      String edition = info.getContentVersion();
      if (edition != null) request.setAttribute("edition", edition);
      List<String> categories = info.getCategories();
      if (categories != null) request.setAttribute("categories", Utilities.join(categories));
      String copies = request.getParameter("copies");
      if (copies != null) request.setAttribute("copies", copies);
      String callNo = request.getParameter("callNo");
      if (callNo != null) request.setAttribute("callNo", callNo);
      Integer noOfPages = info.getPageCount();
      if (noOfPages != null) request.setAttribute("noOfPages", noOfPages);
      String description = info.getDescription();
      if (description!= null) request.setAttribute("description", description);
      request.setAttribute("autocompletesUsingGoogleBooksID", true);
      request.setAttribute("autoFillUsingAttribute", true);
      request.getRequestDispatcher("/staff/books/add/index.jsp").forward(request, response);
      return;
    }
  }
%>

<div class="panel panel-default">
  <div class="panel-heading">
    <form action="/staff/books/add/" method="get" class="form-horizontal" role="form">
      <input type="hidden" name="autocompleteUsingGoogleBooks" value="true" />
      <div class="input-group">
        <span class="input-group-addon hidden-xs"><span class="glyphicon glyphicon-book"></span></span>
        <input type="text" class="form-control" id="inputAutoGoogleBooksID" {% set_attribute_named_when_autofill googleBooksID%} placeholder="Google Books ID" />
        <span class="input-group-btn">
          <input type="submit" class="btn btn-primary col-sx-12" value="Autocomplete Using Google Books" />
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
      <p><%=ex.getLocalizedMessage()%></p>
    </details>
  </div>
  <% } %>
  <div class="panel-body">
    <form action="/staff/books/add/" method="post" class="form-horizontal" role="form">
      <div class="form-group">
        <label for="inputISBN" class="col-sm-2 control-label">ISBN</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="inputISBN" {% set_attribute_named_when_autofill isbn%} placeholder="ISBN" required />
        </div>
      </div>
      <div class="form-group">
        <label for="inputTitle" class="col-sm-2 control-label">Title</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="inputTitle" {% set_attribute_named_when_autofill title%} placeholder="Title" required />
        </div>
      </div>
      <div class="form-group">
        <label for="inputAuthors" class="col-sm-2 control-label">Authors</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="inputAuthors" {% set_attribute_named_when_autofill authors%} placeholder="Authors" required />
        </div>
      </div>
      <div class="form-group">
        <label for="inputPublisher" class="col-sm-2 control-label">Publisher</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="inputPublisher" {% set_attribute_named_when_autofill publisher%} placeholder="Publisher" required />
        </div>
      </div>
      <div class="form-group">
        <label for="inputPublicationYear" class="col-sm-2 control-label">Publication Year</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="inputPublicationYear" {% set_attribute_named_when_autofill publicationYear%} placeholder="Publication Year" />
        </div>
      </div>
      <div class="form-group">
        <label for="inputEdition" class="col-sm-2 control-label">Edition</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="inputEdition" {% set_attribute_named_when_autofill edition%} placeholder="Edition" />
        </div>
      </div>
      <div class="form-group">
        <label for="inputcategories" class="col-sm-2 control-label">categories</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="inputcategories" {% set_attribute_named_when_autofill categories%} placeholder="categories" />
        </div>
      </div>
      <div class="form-group">
        <label for="inputNoOfPages" class="col-sm-2 control-label">No. of Pages</label>
        <div class="col-sm-10">
          <input type="number" class="form-control" id="inputNoOfPages" {% set_attribute_named_when_autofill noOfPages%} placeholder="No. of Pages" min="1" required />
        </div>
      </div>
      <div class="form-group">
        <label for="inputCopies" class="col-sm-2 control-label">No. of Copies</label>
        <div class="col-sm-10">
          <input type="number" class="form-control" id="inputCopies" {% set_attribute_named_when_autofill copies%} placeholder="Copies" value="1" min="1" required />
        </div>
      </div>
      <div class="form-group">
        <label for="inputCallNo" class="col-sm-2 control-label">Call No.</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="inputCallNo" {% set_attribute_named_when_autofill callNo%} placeholder="Call No." />
        </div>
      </div>
      <div class="form-group">
        <label for="inputGoogleBooksID" class="col-sm-2 control-label">Google Books ID</label>
        <div class="col-sm-10">
          <div class="input-group">
            <span class="input-group-addon"><span class="glyphicon glyphicon-book"></span></span>
            <input type="text" class="form-control" id="inputGoogleBooksID" {% set_attribute_named_when_autofill googleBooksID%} placeholder="Google Books ID" />
          </div>
        </div>
      </div>
      <div class="form-group">
        <label for="inputDescription" class="col-sm-2 control-label">Description</label>
        <div class="col-sm-10">
          <textarea class="form-control" rows="5" id="inputDescription" name="description" placeholder="Description">{% set_attribute_when_autofill description%}</textarea>
        </div>
      </div>
      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <input type="submit" class="btn btn-primary" value="Add" />
          <input type="reset" class="btn btn-danger pull-right" value="Reset Form" />
        </div>
      </div>
    </form>
  </div>
</div>
