package com.abhishekmunie.librarymanagementsystem.staff;

import java.sql.*;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.abhishekmunie.librarymanagementsystem.Utilities;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class Staff extends com.abhishekmunie.librarymanagementsystem.user.User {

	public static final String EntityName = "Staff";

	public enum Lable {
		EmailId("emailid");

		private String lable;

		Lable(String lable) {
			this.lable = lable;
		}

		public String toString() {
			return lable;
		}
	}

	protected String emailid;

	/* Static Functions */

	public static boolean isStaff(String email) throws SQLException {
		Connection conn = null;
		int count = 0;
		String statement = "SELECT count(*) as isStaff FROM lms.Staff WHERE "
				+ Lable.EmailId + " = ? ";
		PreparedStatement stmt = null;
		try {
			conn = Utilities.getSQLConnection();
			stmt = conn.prepareStatement(statement);
			stmt.setString(1, email);
			ResultSet staffRS = stmt.executeQuery();
			staffRS.first();
			count = staffRS.getInt("isStaff");
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return (count != 0);
	}

	public static boolean isCurrentUserStaff() throws SQLException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		return isStaff(user.getEmail());
	}

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

	public static Vector<Staff> getAllStaff() throws SQLException {
		Connection conn = null;
		Vector<Staff> result = null;
		try {
			conn = Utilities.getSQLConnection();
			ResultSet staffsRS = conn.createStatement().executeQuery(
					"SELECT * FROM lms."+EntityName);
			result = new Vector<Staff>();
			while (staffsRS.next()) {
				result.addElement(new Staff(staffsRS));
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
		String statement = "INSERT INTO lms." + EntityName + " VALUES( ? )";
		PreparedStatement stmt = conn.prepareStatement(statement);
		return stmt;
	}

	public static int addStaffIntoDatabaseWithEmail(String email)
			throws SQLException {
		int success = 2;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = Utilities.getSQLConnection();
			stmt = getInsertStatement(conn);
			stmt.setString(1, email);
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

	public static int deleteStaffIntoDatabaseWithEmail(String email)
			throws SQLException {
		int success = 2;
		Connection conn = null;
		String statement = "DELETE FROM lms." + EntityName + " WHERE "
				+ Lable.EmailId + " = ? ";
		PreparedStatement stmt = null;
		try {
			conn = Utilities.getSQLConnection();
			stmt = conn.prepareStatement(statement);
			stmt.setString(1, email);
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

	/* Constructors */

	/**
	 * @param emailid
	 */
	public Staff(String emailid) {
		super();
		this.emailid = emailid;
	}

	public Staff(HttpServletRequest req) {
		this.emailid = req.getParameter("emailid");
	}

	public Staff(ResultSet rs) throws SQLException {
		try {
			this.emailid = rs.getString(Lable.EmailId.toString());
		} catch (SQLException e) {
			this.emailid = null;
		}
	}

	/* Getter/Setters */

	/**
	 * @return the emailid
	 */
	public String getEmailid() {
		return emailid;
	}

}
