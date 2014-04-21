---
layout: default
---

<div class="container-fluid">
  <div class="row">
    <div class="col-sm-3 col-md-2">
      <ul class="nav nav-pills nav-stacked nav-sidebar">
        <li class="{% is_active /cl/index.jsp%}" ><a href="/cl" >Dashboard</a></li>
        <li role="presentation" class="divider"></li>
        <li class="{% is_active_level /cl/staff-management%}" ><a href="/cl/staff-management/" >Staff Management</a></li>
        <li class="{% is_active_level /cl/book-requests/%}" ><a href="/cl/book-requests/" >Book Requests</a></li>
        <li class="{% is_active_level /cl/analytics/%}" ><a href="/cl/analytics/" >Analytics</a></li>
        <li class="{% is_active_level /cl/settings/%}" ><a href="/cl/settings/" >Settings</a></li>
      </ul>
    </div>
    <div class="col-sm-9 col-md-10 main">
      <div class="container-fluid">
        {{content}}
      </div>
    </div>
  </div>
</div>