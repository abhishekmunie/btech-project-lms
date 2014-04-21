<!--[if lt IE 7]>
<div id="dev-warnng" class="alert alert-danger">
  <p class="chromeframe">
    You are using an <strong>outdated</strong> browser.
    Please <a href="http://browsehappy.com/" class="alert-link>upgrade your browser</a> or
    <a href="http://www.google.com/chromeframe/?redirect=true" class="alert-link>activate Google Chrome Frame</a> to improve your experience.
  </p>
</div>
<![endif]-->
<% if (session.getAttribute("no-project-warning") == null) { %>
<div id="dev-warnng" class="alert alert-warning alert-dismissable">
  <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
  <a href="/no-project-warning?redirect=<%=Utilities.urlEncodeUTF8(request.getRequestURI().replaceAll("/index.jsp", "/"))%>" class="alert-link pull-right" >Dont show again</a>
  <strong>Warning!</strong> This is an academic project, not the official library page.
  It's open source and available under MIT Licence at <a href="https://github.com/abhishekmunie/btech-project-lms" target="_blank" class="alert-link">GitHub</a>
</div>
<% } %>
