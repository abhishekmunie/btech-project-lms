package com.abhishekmunie.librarymanagementsystem.books;

import java.sql.*;
import java.util.Vector;

import com.abhishekmunie.librarymanagementsystem.DBEntity;
import com.abhishekmunie.librarymanagementsystem.Utilities;

/**
 * 
 * @author abhishekmunie
 */
public class Category extends DBEntity {

	public static final String EntityName = "Category";

	/**
	 * 
	 * @author abhishekmunie
	 */
	public enum Lable {
		ISBN("ISBN"), Name("CategoryName");

		private String lable;

		Lable(String lable) {
			this.lable = lable;
		}

		public String toString() {
			return lable;
		}
	}

	public String name;
	protected Book book;

	/* Static Functions */

	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static long getCount() throws SQLException {
		Connection conn = null;
		long totalCount = 0;
		try {
			conn = Utilities.getSQLConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			ResultSet countRS = conn.createStatement().executeQuery(
					"SELECT COUNT(*) as totalCount FROM lms." + EntityName);
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

	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static long getDistinctCount() throws SQLException {
		Connection conn = null;
		long totalCount = 0;
		try {
			conn = Utilities.getSQLConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			ResultSet countRS = conn.createStatement().executeQuery(
					"SELECT COUNT(DISTINCT " + Lable.Name
							+ ") as totalCount FROM lms." + EntityName);
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

	/**
	 * 
	 * @param categoryName
	 * @return
	 * @throws SQLException
	 */
	public static Vector<String> getBooksISBNForCategoryWithName(
			String categoryName) throws SQLException {
		Vector<String> bookISBNs = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = Utilities.getSQLConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = conn.prepareStatement("SELECT " + Lable.ISBN + " FROM lms."
					+ EntityName + " WHERE " + Lable.Name + " = ?");
			stmt.setString(1, categoryName);
			ResultSet isbnRS = stmt.executeQuery();
			bookISBNs = new Vector<String>();
			while (isbnRS.next()) {
				bookISBNs.addElement(isbnRS.getString(Lable.ISBN.toString()));
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return bookISBNs;
	}

	/**
	 * 
	 * @param book
	 * @return
	 * @throws SQLException
	 */
	public static Vector<Category> getCatagoriesForBook(Book book)
			throws SQLException {
		Vector<Category> catagories = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = Utilities.getSQLConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = conn.prepareStatement("SELECT " + Lable.Name + " FROM lms."
					+ EntityName + " WHERE " + Lable.ISBN + " = ?");
			stmt.setString(1, book.getISBN());
			ResultSet catagoriesRS = stmt.executeQuery();
			catagories = new Vector<Category>();
			while (catagoriesRS.next()) {
				catagories.addElement(new Category(catagoriesRS
						.getString(Lable.Name.toString()), book));
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return catagories;
	}

	/**
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static PreparedStatement getInsertStatement(Connection conn)
			throws SQLException {
		String statement = "INSERT INTO lms." + EntityName + " VALUES( ? , ? )";
		PreparedStatement stmt = conn.prepareStatement(statement);
		return stmt;
	}

	/* Constructors */

	/**
	 * 
	 * @param name
	 *            the name of category
	 * @param book
	 *            the book whose category this object represents
	 */
	public Category(String name, Book book) {
		this.name = name;
		this.book = book;
	}

	/**
	 * 
	 * @param rs
	 * @param book
	 * @throws SQLException
	 */
	public Category(ResultSet rs, Book book) throws SQLException {
		this(rs.getString(Lable.Name.toString()), book);
	}

	/* Member Function */

	public int insertIntoDataBase(Connection conn) throws SQLException {
		int success = 2;
		PreparedStatement stmt = null;
		try {
			stmt = getInsertStatement(conn);
			stmt.setString(1, this.book.isbn);
			stmt.setString(2, this.name);
			success = stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return success;
	}

	/**
	 * 
	 * @param conn
	 * @param batchStmt
	 * @return
	 * @throws SQLException
	 */
	public int insertIntoDataBaseUsingBatch(PreparedStatement batchStmt)
			throws SQLException {
		int success = 2;
		batchStmt.setString(1, this.book.isbn);
		batchStmt.setString(2, this.name);
		batchStmt.addBatch();
		return success;
	}

	/* Getter/Setters */

	/**
	 * @return the category name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the book whose category this object represents
	 */
	public Book getBook() {
		return book;
	}

}
