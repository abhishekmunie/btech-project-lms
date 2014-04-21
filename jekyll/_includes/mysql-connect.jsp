<%
  if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
    conn = DriverManager.getConnection(url);
  } else {
    conn = DriverManager.getConnection(url, CONFIG.MySQLUser, CONFIG.MySQLPass);
  }
%>
