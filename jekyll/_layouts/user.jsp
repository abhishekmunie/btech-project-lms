---
layout: default
---

<div class="container-fluid">
  <div class="row">
    <div class="col-sm-3 col-md-2">
      <ul class="nav nav-pills nav-stacked nav-sidebar">
        <li class="{% is_active /user/index.jsp%}" ><a href="/user" >Dashboard</a></li>
        <li role="presentation" class="divider"></li>
        <li class="{% is_active_level /user/current-issues%}" ><a href="/user/current-issues/" >Current Issue</a></li>
        <li class="{% is_active_level /user/book-requests%}" ><a href="/user/request-book/" >Request Book</a></li>
        <li class="{% is_active_level /user/settings%}" ><a href="/user/settings/" >Settings</a></li>
      </ul>
    </div>
    <div class="col-sm-9 col-md-10 main">
      <div class="container-fluid">
        {{content}}
      </div>
    </div>
  </div>
</div>