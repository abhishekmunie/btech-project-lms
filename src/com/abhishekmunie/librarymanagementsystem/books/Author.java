package com.abhishekmunie.librarymanagementsystem.books;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.abhishekmunie.librarymanagementsystem.DBEntity;
import com.abhishekmunie.librarymanagementsystem.Utilities;

/**
 * 
 * @author abhishekmunie
 */
public class Author extends DBEntity {

	public static final String EntityName = "Author";

	/**
	 * 
	 * @author abhishekmunie
	 */
	public enum Lable {
		ISBN("ISBN"), Name("AuthorName");

		private String lable;

		Lable(String lable) {
			this.lable = lable;
		}

		public String toString() {
			return lable;
		}
	}

	protected String name;
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
					"SELECT COUNT(DISTINCT " + Lable.ISBN
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
	 * @param authorName
	 * @return
	 * @throws SQLException
	 */
	public static Vector<String> getBooksISBNForAuthorWithName(String authorName)
			throws SQLException {
		Vector<String> bookISBNs = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = Utilities.getSQLConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = conn.prepareStatement("SELECT " + Lable.ISBN + " FROM lms."
					+ EntityName + " WHERE " + Lable.Name + " = ?");
			stmt.setString(1, authorName);
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
	public static Vector<Author> getAuthorsForBook(Book book)
			throws SQLException {
		Vector<Author> authors = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = Utilities.getSQLConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = conn.prepareStatement("SELECT " + Lable.Name + " FROM lms."
					+ EntityName + " WHERE " + Lable.ISBN + " = ?");
			stmt.setString(1, book.getISBN());
			ResultSet authorsRS = stmt.executeQuery();
			authors = new Vector<Author>();
			while (authorsRS.next()) {
				authors.addElement(new Author(authorsRS.getString(Lable.Name
						.toString()), book));
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return authors;
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
	 *            the name of the author
	 * @param book
	 *            the book whose author this object represents
	 */
	public Author(String name, Book book) {
		this.name = name;
		this.book = book;
	}

	/* Member Function */

	/**
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
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
	 * @return the name of the author
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the book whose author this object represents
	 */
	public Book getBook() {
		return book;
	}
}
