package com.abhishekmunie.librarymanagementsystem.books;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.abhishekmunie.librarymanagementsystem.Utilities;
import com.abhishekmunie.librarymanagementsystem.books.Issue.Lable;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class Reissue {

	public static final String EntityName = "Reissue";

	public enum Lable {
		IssueID("IssueID"), ReissueDate("ReissueDate"), ReissuerID("ReissuerID"), ReissuerEmail(
				"ReissuerEmail");

		private String lable;

		Lable(String lable) {
			this.lable = lable;
		}

		public String toString() {
			return lable;
		}
	}

	protected int issueID;
	protected Date reissueDate;
	protected String reissuerID;
	protected String reissuerEmail;

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

	public static long getReissueCountForIssueWithId(int issueID)
			throws SQLException {
		Connection conn = null;
		long totalCount = 0;
		String statement = "SELECT count(*) as count FROM lms." + EntityName
				+ " WHERE " + Lable.IssueID + " = ? ";
		PreparedStatement stmt = null;
		try {
			conn = Utilities.getSQLConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = conn.prepareStatement(statement);
			stmt.setInt(1, issueID);
			ResultSet countRS = stmt.executeQuery();
			if (countRS.first()) {
				totalCount = countRS.getLong("count");
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return totalCount;
	}

	public static Vector<Reissue> getAllReissues() throws SQLException {
		Connection conn = null;
		Vector<Reissue> result = null;
		try {
			conn = Utilities.getSQLConnection();
			ResultSet reissuesRS = conn.createStatement().executeQuery(
					"SELECT * FROM lms." + EntityName);
			result = new Vector<Reissue>();
			while (reissuesRS.next()) {
				result.addElement(new Reissue(reissuesRS));
			}
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return result;
	}

	public static PreparedStatement getInsertStatement(Connection conn)
			throws SQLException {
		String statement = "INSERT INTO lms." + EntityName
				+ " VALUES (?, ?, ?, ?) ";
		PreparedStatement stmt = conn.prepareStatement(statement);
		return stmt;
	}

	/* Constructors */

	public Reissue(HttpServletRequest req) throws NumberFormatException {
		this.issueID = Integer.parseInt(req.getParameter("issueID"));
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		this.reissuerID = user.getUserId();
		this.reissuerEmail = user.getEmail();
		this.reissueDate = new Date();
	}

	public Reissue(ResultSet rs) throws SQLException {
		try {
			this.issueID = rs.getInt(Lable.IssueID.toString());
		} catch (SQLException e) {
			this.issueID = -1;
		}
		try {
			this.reissuerID = rs.getString(Lable.ReissuerID.toString());
		} catch (SQLException e) {
			this.reissuerID = null;
		}
		try {
			this.reissuerEmail = rs.getString(Lable.ReissuerEmail.toString());
		} catch (SQLException e) {
			this.reissuerEmail = null;
		}
		try {
			this.reissueDate = rs.getDate(Lable.ReissueDate.toString());
		} catch (SQLException e) {
			this.reissueDate = null;
		}
	}

	/* Member Function */

	public int addReissueIntoDatabase() throws SQLException {
		int success = 2;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = Utilities.getSQLConnection();
			stmt = getInsertStatement(conn);
			stmt.setInt(1, this.issueID);
			stmt.setDate(2, new java.sql.Date(this.reissueDate.getTime()));
			stmt.setString(3, this.reissuerID);
			stmt.setString(4, this.reissuerEmail);
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
	 * @return the reissueDate
	 */
	public Date getReissueDate() {
		return reissueDate;
	}

	/**
	 * @return the reissuerID
	 */
	public String getReissuerID() {
		return reissuerID;
	}

	/**
	 * @return the reissuerEmail
	 */
	public String getReissuerEmail() {
		return reissuerEmail;
	}

}
