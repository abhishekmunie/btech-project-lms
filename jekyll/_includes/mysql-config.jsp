<%
  String url = null;
  if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
    // Load the class that provides the new "jdbc:google:mysql://" prefix.
    Class.forName("com.mysql.jdbc.GoogleDriver");
    url = "jdbc:google:mysql://library-management-system:your-instance-name/lms?user=root";
  } else {
    // Local MySQL instance to use during development.
    Class.forName("com.mysql.jdbc.Driver");
    url = CONFIG.MySQLURL;
  }
  Connection conn;
%>
