package com.abhishekmunie.librarymanagementsystem.books;

import java.sql.*;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.abhishekmunie.librarymanagementsystem.Utilities;

/**
 * 
 * @author abhishekmunie
 */
public class AvailableBook extends Book {

	public static final String EntityName = "AvailableBook";

	public enum Lable {
		ISBN("ISBN"), TotalCopies("TotalCopies"), CallNumber("CallNumber");

		private String lable;

		Lable(String lable) {
			this.lable = lable;
		}

		public String toString() {
			return lable;
		}
	}

	public int copies;
	public String callNo;

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

	public static AvailableBook getAvailableBookForISBN(String isbn)
			throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		AvailableBook result = null;
		try {
			conn = Utilities.getSQLConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = conn.prepareStatement("SELECT * FROM lms."
					+ AvailableBook.EntityName + " NATURAL JOIN lms."
					+ Book.EntityName + " WHERE ISBN = ?");
			stmt.setString(1, isbn);
			ResultSet rs = stmt.executeQuery();
			if (rs.first()) {
				result = new AvailableBook(rs);
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

	public static Vector<AvailableBook> getAllAvailableBooks(String fields)
			throws SQLException {
		Vector<AvailableBook> result = null;
		Connection conn = null;
		try {
			conn = Utilities.getSQLConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			ResultSet rs = conn.createStatement().executeQuery(
					"SELECT " + fields + " FROM lms."
							+ AvailableBook.EntityName + " NATURAL JOIN lms."
							+ Book.EntityName);
			result = new Vector<AvailableBook>();
			while (rs.next()) {
				result.addElement(new AvailableBook(rs));
			}
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return result;
	}

	public static Vector<AvailableBook> getAvailableBooks() throws SQLException {
		return getAllAvailableBooks("*");
	}

	public static Vector<AvailableBook> getAllAvailableBooksInRange(
			String fields, long start, long length) throws SQLException {
		Vector<AvailableBook> result = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = Utilities.getSQLConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = conn.prepareStatement("SELECT " + fields + " FROM lms."
					+ AvailableBook.EntityName + " NATURAL JOIN lms."
					+ Book.EntityName + " LIMIT ?,?");
			stmt.setLong(1, start);
			stmt.setLong(2, length);
			ResultSet rs = stmt.executeQuery();
			result = new Vector<AvailableBook>();
			while (rs.next()) {
				result.addElement(new AvailableBook(rs));
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

	public static Vector<AvailableBook> getAvailableBooksInRange(long start,
			long length) throws SQLException {
		return getAllAvailableBooksInRange("*", start, length);
	}

	public static Vector<AvailableBook> getAllAvailableBooksWithAuthorsAndCatagoriesInRange(
			String fields, long start, long length) throws SQLException {
		Vector<AvailableBook> result = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = Utilities.getSQLConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = conn.prepareStatement("SELECT " + fields
					+ ",GROUP_CONCAT(DISTINCT " + Author.Lable.Name
					+ " SEPARATOR ',') as " + Book.Lable.Authors
					+ ",GROUP_CONCAT(DISTINCT " + Category.Lable.Name
					+ " SEPARATOR ',') as " + Book.Lable.Categories
					+ " FROM lms." + AvailableBook.EntityName
					+ " NATURAL JOIN lms." + Book.EntityName
					+ " NATURAL LEFT JOIN lms." + Author.EntityName
					+ " NATURAL LEFT JOIN lms." + Category.EntityName
					+ " GROUP BY " + Book.Lable.ISBN + " LIMIT ?,?");
			stmt.setLong(1, start);
			stmt.setLong(2, length);
			ResultSet rs = stmt.executeQuery();
			result = new Vector<AvailableBook>();
			while (rs.next()) {
				result.addElement(new AvailableBook(rs));
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

	public static Vector<AvailableBook> getAllAvailableBooksWithAuthorsAndCatagoriesInRange(
			long start, long length) throws SQLException {
		return getAllAvailableBooksWithAuthorsAndCatagoriesInRange("*", start,
				length);
	}

	public static PreparedStatement getInsertStatement(Connection conn)
			throws SQLException {
		String statement = "INSERT INTO lms." + EntityName
				+ " VALUES( ? , ? , ? )";
		PreparedStatement stmt = conn.prepareStatement(statement);
		return stmt;
	}

	/* Constructors */

	/**
	 * @param isbn
	 * @param title
	 * @param publisher
	 * @param publicationYear
	 * @param edition
	 * @param description
	 * @param googleBooksID
	 * @param noOfPages
	 * @param authors
	 * @param categories
	 * @param copies
	 * @param callNo
	 */
	public AvailableBook(String isbn, String title, String publisher,
			String publicationYear, String edition, String description,
			String googleBooksID, int noOfPages, Author[] authors,
			Category[] categories, int copies, String callNo) {
		super(isbn, title, publisher, publicationYear, edition, description,
				googleBooksID, noOfPages, authors, categories);
		this.copies = copies;
		this.callNo = callNo;
	}

	public AvailableBook(HttpServletRequest req) {
		super(req);
		this.copies = 0;
		try {
			this.copies = Integer.parseInt(req.getParameter("copies"));
		} catch (NumberFormatException e) {
		}
		this.callNo = req.getParameter("callNo");
	}

	public AvailableBook(ResultSet rs) throws SQLException {
		super(rs);
		try {
			this.copies = rs.getInt(Lable.TotalCopies.toString());
		} catch (SQLException e) {
			this.copies = 0;
		}
		try {
			this.callNo = rs.getString(Lable.CallNumber.toString());
		} catch (SQLException e) {
			this.callNo = null;
		}
	}

	/* Member Function */

	public int insertAvailableBookIntoDataBaseUsingPreparedStatement(PreparedStatement stmt)
			throws SQLException {
		int success = 2;
		stmt.setString(1, this.isbn);
		stmt.setInt(2, this.copies);
		stmt.setString(3, this.callNo);
		success = stmt.executeUpdate();
		return success;
	}

	public int insertIntoDataBaseUsingConnection(Connection conn)
			throws SQLException {
		super.insertIntoDataBaseUsingConnection(conn);
		int success = 2;
		PreparedStatement stmt = null;
		try {
			stmt = getInsertStatement(conn);
			success = insertAvailableBookIntoDataBaseUsingPreparedStatement(stmt);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return success;
	}

	/* Getter/Setters */

	/**
	 * @return the copies
	 */
	public int getTotalCopies() {
		return copies;
	}

	public int getAvailableCopies() {
		System.err.println("Unimplementsd: AvailableBook.getAvailableCopies()");
		return 0;
	}

	/**
	 * @return the callNo
	 */
	public String getCallNo() {
		return callNo;
	}
	

}
