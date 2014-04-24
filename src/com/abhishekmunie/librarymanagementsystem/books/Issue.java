package com.abhishekmunie.librarymanagementsystem.books;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.abhishekmunie.librarymanagementsystem.Utilities;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class Issue {

	public static final String EntityName = "Issue";

	public enum Lable {
		IssueID("IssueID"), BookISBN("BookISBN"), UserID("UserID"), UserEmail(
				"UserEmail"), IssuerID("IssuerID"), IssuerEmail("IssuerEmail"), IssueDate(
				"IssueDate"), ReturnedDate("ReturnedDate");

		private String lable;

		Lable(String lable) {
			this.lable = lable;
		}

		public String toString() {
			return lable;
		}
	}

	protected int issueID;
	protected String bookISBN;
	protected String userID;
	protected String userEmail;
	protected String issuerID;
	protected String issuerEmail;
	protected Date issueDate;
	protected Date returnDate;

	/* Static Functions */

	public static long getTotalCount() throws SQLException {
		Connection conn = null;
		long totalCount = 0;
		try {
			conn = Utilities.getSQLConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			ResultSet countRS = conn.createStatement().executeQuery(
					"SELECT count(*) as totalCount FROM lms." + EntityName);
			if (countRS.first()) {
				totalCount = countRS.getLong("totalCount");
			}
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return totalCount;
	}

	public static Vector<Issue> getAllIssues() throws SQLException {
		Connection conn = null;
		Vector<Issue> result = null;
		try {
			conn = Utilities.getSQLConnection();
			ResultSet issuesRS = conn.createStatement().executeQuery(
					"SELECT * FROM lms." + EntityName);
			result = new Vector<Issue>();
			while (issuesRS.next()) {
				result.addElement(new Issue(issuesRS));
			}
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return result;
	}

	public static Vector<Issue> getCurrentIssues() throws SQLException {
		Connection conn = null;
		Vector<Issue> result = null;
		try {
			conn = Utilities.getSQLConnection();
			ResultSet issuesRS = conn.createStatement().executeQuery(
					"SELECT * FROM lms." + EntityName + " WHERE "
							+ Lable.ReturnedDate + " is null");
			result = new Vector<Issue>();
			while (issuesRS.next()) {
				result.addElement(new Issue(issuesRS));
			}
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return result;
	}

	public static Vector<Issue> getAllIssuesForUserWithID(String userID)
			throws SQLException {
		Connection conn = null;
		Vector<Issue> result = null;
		String statement = "SELECT * FROM lms." + EntityName + " WHERE "
				+ Lable.UserID + " = ? ";
		PreparedStatement stmt = null;
		try {
			conn = Utilities.getSQLConnection();
			stmt = conn.prepareStatement(statement);
			stmt.setString(1, userID);
			ResultSet issuesRS = stmt.executeQuery();
			result = new Vector<Issue>();
			while (issuesRS.next()) {
				result.addElement(new Issue(issuesRS));
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return result;
	}

	public static Vector<Issue> getAllIssuesForUserWithEmain(String userEmail)
			throws SQLException {
		Connection conn = null;
		Vector<Issue> result = null;
		String statement = "SELECT * FROM lms." + EntityName + " WHERE "
				+ Lable.UserEmail + " = ? ";
		PreparedStatement stmt = null;
		try {
			conn = Utilities.getSQLConnection();
			stmt = conn.prepareStatement(statement);
			stmt.setString(1, userEmail);
			ResultSet issuesRS = stmt.executeQuery();
			result = new Vector<Issue>();
			while (issuesRS.next()) {
				result.addElement(new Issue(issuesRS));
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return result;
	}

	public static Vector<Issue> getCurrentIssuesForUserWithID(String userID)
			throws SQLException {
		Connection conn = null;
		Vector<Issue> result = null;
		String statement = "SELECT * FROM lms." + EntityName + " WHERE "
				+ Lable.ReturnedDate + " is null AND " + Lable.UserID + " = ? ";
		PreparedStatement stmt = null;
		try {
			conn = Utilities.getSQLConnection();
			stmt = conn.prepareStatement(statement);
			stmt.setString(1, userID);
			ResultSet issuesRS = stmt.executeQuery();
			result = new Vector<Issue>();
			while (issuesRS.next()) {
				result.addElement(new Issue(issuesRS));
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return result;
	}

	public static Vector<Issue> getCurrentIssuesForUserWithEmail(
			String userEmail) throws SQLException {
		Connection conn = null;
		Vector<Issue> result = null;
		String statement = "SELECT * FROM lms." + EntityName + " WHERE "
				+ Lable.ReturnedDate + " is null AND " + Lable.UserEmail
				+ " = ? ";
		PreparedStatement stmt = null;
		try {
			conn = Utilities.getSQLConnection();
			stmt = conn.prepareStatement(statement);
			stmt.setString(1, userEmail);
			ResultSet issuesRS = stmt.executeQuery();
			result = new Vector<Issue>();
			while (issuesRS.next()) {
				result.addElement(new Issue(issuesRS));
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return result;
	}

	public static int addReturnDateIntoDatabaseForIssueID(int issueID)
			throws SQLException {
		int success = 2;
		Connection conn = null;
		String statement = "UPDATE lms." + EntityName + " SET "
				+ Lable.ReturnedDate + " = ? WHERE " + Lable.IssueID + " = ?";
		PreparedStatement stmt = null;
		try {
			conn = Utilities.getSQLConnection();
			stmt = conn.prepareStatement(statement);
			stmt.setDate(1, new java.sql.Date(new Date().getTime()));
			stmt.setInt(2, issueID);
			success = stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return success;
	}

	public static PreparedStatement getInsertStatement(Connection conn)
			throws SQLException {
		String statement = "INSERT INTO lms." + EntityName + " (`"
				+ Lable.BookISBN + "`, `" + Lable.UserID + "`, `"
				+ Lable.UserEmail + "`, `" + Lable.IssuerID + "`, `"
				+ Lable.IssuerEmail + "`, `" + Lable.IssueDate
				+ "`) VALUES(?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(statement);
		return stmt;
	}

	/* Constructors */

	/**
	 * @param issueID
	 * @param bookISBN
	 * @param userID
	 * @param userEmail
	 * @param issuerID
	 * @param issuerEmail
	 * @param issueDate
	 * @param returnDate
	 */
	public Issue(int issueID, String bookISBN, String userID, String userEmail,
			String issuerID, String issuerEmail, Date issueDate, Date returnDate) {
		this.issueID = issueID;
		this.bookISBN = bookISBN;
		this.userID = userID;
		this.userEmail = userEmail;
		this.issuerID = issuerID;
		this.issuerEmail = issuerEmail;
		this.issueDate = issueDate;
		this.returnDate = returnDate;
	}

	public Issue(HttpServletRequest req) {
		this.bookISBN = req.getParameter("bookISBN");
		this.userID = req.getParameter("userID");
		this.userEmail = req.getParameter("userEmail");
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		this.issuerID = user.getUserId();
		this.issuerEmail = user.getEmail();
		this.issueDate = new Date();
	}

	public Issue(ResultSet rs) throws SQLException {
		try {
			this.issueID = rs.getInt(Lable.IssueID.toString());
		} catch (SQLException e) {
			this.issueID = -1;
		}
		try {
			this.bookISBN = rs.getString(Lable.BookISBN.toString());
		} catch (SQLException e) {
			this.bookISBN = null;
		}
		try {
			this.userID = rs.getString(Lable.UserID.toString());
		} catch (SQLException e) {
			this.userID = null;
		}
		try {
			this.userEmail = rs.getString(Lable.UserEmail.toString());
		} catch (SQLException e) {
			this.userEmail = null;
		}
		try {
			this.issuerID = rs.getString(Lable.IssuerID.toString());
		} catch (SQLException e) {
			this.issuerID = null;
		}
		try {
			this.issuerEmail = rs.getString(Lable.IssuerEmail.toString());
		} catch (SQLException e) {
			this.issuerEmail = null;
		}
		try {
			this.issueDate = rs.getDate(Lable.IssueDate.toString());
		} catch (SQLException e) {
			this.issueDate = null;
		}
		try {
			this.returnDate = rs.getDate(Lable.ReturnedDate.toString());
		} catch (SQLException e) {
			this.returnDate = null;
		}
	}

	/* Member Function */

	public int addIssueIntoDatabase() throws SQLException {
		int success = 2;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = Utilities.getSQLConnection();
			stmt = getInsertStatement(conn);
			stmt.setString(1, this.bookISBN);
			stmt.setString(2, this.userID);
			stmt.setString(3, this.userEmail);
			stmt.setString(4, this.issuerID);
			stmt.setString(5, this.issuerEmail);
			stmt.setDate(6, new java.sql.Date(this.issueDate.getTime()));
			success = stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return success;
	}

	/* Getter/Setters */

	/**
	 * @return the issueID
	 */
	public int getIssueID() {
		return issueID;
	}

	/**
	 * @return the bookISBN
	 */
	public String getBookISBN() {
		return bookISBN;
	}

	/**
	 * @return the userID
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * @return the issuerID
	 */
	public String getIssuerID() {
		return issuerID;
	}

	/**
	 * @return the issuerEmail
	 */
	public String getIssuerEmail() {
		return issuerEmail;
	}

	/**
	 * @return the issueDate
	 */
	public Date getIssueDate() {
		return issueDate;
	}

	/**
	 * @return the returnDate
	 */
	public Date getReturnDate() {
		return returnDate;
	}

}
