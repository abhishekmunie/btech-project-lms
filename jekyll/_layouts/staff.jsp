---
layout: default
---

<div class="container-fluid">
  <div class="row">
    <div class="col-sm-3 col-md-2">
      <ul id="staff-nav-accordion" class="nav nav-pills nav-stacked nav-sidebar" >
        <li class="{% is_active /staff/index.jsp%}"><a class="btn" href="/staff">Dashboard</a></li>
        <li class="{% is_active_level /staff/books%}">
            <a class="btn" data-toggle="collapse" data-parent="#staff-nav-accordion" href="#staff-books-nav">Books</a>
            <div id="staff-books-nav" class="collapse {% if_active_level_set_in /staff/books%}">
              <ul class="nav nav-pills nav-stacked">
                <li class="{% is_active_level /staff/books/add%}"><a href="/staff/books/add">Add</a></li>
                <li class="{% is_active_level /staff/books/issue%}"><a href="/staff/books/issue">Issue</a></li>
                <li class="{% is_active_level /staff/books/reissue%}"><a href="/staff/books/reissue">Reisue</a></li>
                <li class="{% is_active_level /staff/books/return%}"><a href="/staff/books/return">Return</a></li>
                <li class="{% is_active_level /staff/books/approved-requests%}"><a href="/staff/books/approved-requests">Approved Requests</a></li>
              </ul>
            </div>
        </li>
        <li class="{% is_active_level /staff/settings%}"><a class="btn" href="/staff/settings">Settings</a></li>
      </ul>
    </div>
    <div class="col-sm-9 col-md-10 main">
      <div class="container-fluid">
        {{content}}
      </div>
    </div>
  </div>
</div>