  <!--
<div class="navbar-wrapper">
  <div class="container">
-->
   <nav class="navbar navbar-inverse navbar-vit navbar-static-top" role="navigation">
      <div class="container-fluid">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="/">{{ site.name }}</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li class="{% if page.url == '/index.jsp' %}active{% endif %}"><a href="/"> <i class="fa fa-home fa-lg"></i> </a></li>
            {% for l1 in site.main_nav %}<li class="{% is_active_level {{l1[1]}}%}"><a href="{{l1[1]}}">{{l1[0]}}</a></li>
            {% endfor %}
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <%
              UserService userService = UserServiceFactory.getUserService();
              User user = userService.getCurrentUser();
              if (user != null) {
                pageContext.setAttribute("user", user);
            %>
            <li class="dropdown {% is_active_level /user%} {% is_active_level /staff%} {% is_active_level /cl%}">
              <a class="btn dropdown-toggle" data-toggle="dropdown" href="/dashboard/">
                <span class="glyphicon glyphicon-user"></span>
                ${fn:escapeXml(user.nickname)} <!-- <span class="badge">33</span> --> <span class="fa fa-caret-down"></span>
              </a>
              <ul class="dropdown-menu">
                <li><a href="/dashboard/"><i class="fa fa-user fa-fw"></i> Dashboard</a></li>
                <li><a href="/dashboard/settings"><i class="fa fa-cogs fa-fw"></i> Settings</a></li>
                <li class="divider"></li>
                <li class="">
                  <a href="<%=userService.createLogoutURL("/")%>">
                    <i class="fa fa-ban fa-fw"></i> Sign out
                  </a>
                </li>
              </ul>
            </li>
            <% } else { %>
            <li><a href="<%=userService.createLoginURL(request.getRequestURI().replaceAll("/index.jsp", "/"))%>">Sign in</a></li>
            <% } %>
          </ul>
          <form action="/search" method="get" class="navbar-form navbar-right">
            <input type="search" name="q" class="form-control" placeholder="Search Books&hellip;" required <%= (request.getRequestURI().startsWith("/search") && request.getParameter("q") != null) ? ("value=\""+request.getParameter("q")+"\"") : "" %>>
          </form>
        </div><!--/.nav-collapse -->
      </div>
    </nav>
<!--
  </div>
</div>
-->
