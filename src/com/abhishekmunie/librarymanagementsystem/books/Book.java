package com.abhishekmunie.librarymanagementsystem.books;

import java.sql.*;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.abhishekmunie.librarymanagementsystem.DBEntity;
import com.abhishekmunie.librarymanagementsystem.Utilities;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Document.Builder;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutResponse;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.DeleteException;
import com.google.appengine.api.search.StatusCode;

/**
 * 
 * @author abhishekmunie
 */
public class Book extends DBEntity {

	public static final String EntityName = "Book";

	public enum Lable {
		ISBN("ISBN"), Title("Title"), Publisher("Publisher"), Edition("Edition"), PublicationYear(
				"PublicationYear"), GoogleBooksID("GoogleBooksID"), NoOfPages(
				"NoOfPages"), Description("Description"), Authors("Authors"), Categories(
				"Categories");

		private String lable;

		Lable(String lable) {
			this.lable = lable;
		}

		public String toString() {
			return lable;
		}
	}

	public static final String INDEX_NAME = "Books";
	public static final int INDEX_PUT_RETRY_LIMIT = 3;
	public static final int INDEX_DELETE_RETRY_LIMIT = 3;

	protected String isbn;
	protected String title;
	protected String publisher;
	protected String publicationYear;
	protected String edition;
	protected String description;
	protected String googleBooksID;
	protected int noOfPages;
	protected Author authors[];
	protected Category categories[];

	/* Static Functions */

	public static Index getIndex() {
		IndexSpec indexSpec = IndexSpec.newBuilder().setName(INDEX_NAME)
				.build();
		Index index = SearchServiceFactory.getSearchService().getIndex(
				indexSpec);
		return index;
	}

	public static long getTotalCount() throws SQLException {
		long totalCount = 0;
		Connection conn = null;
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

	public static Book getBookForISBN(String isbn) throws SQLException {
		Book result = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = Utilities.getSQLConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = conn.prepareStatement("SELECT * FROM lms." + Book.EntityName
					+ " WHERE ISBN = ?");
			stmt.setString(1, isbn);
			ResultSet rs = stmt.executeQuery();
			if (rs.first()) {
				result = new Book(rs);
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

	public static Book getBookWithAuthorsAndCatagoriesForISBN(String isbn)
			throws SQLException {
		Book result = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = Utilities.getSQLConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = conn.prepareStatement("SELECT *,GROUP_CONCAT(DISTINCT "
					+ Author.Lable.Name + " SEPARATOR ',') as "
					+ Book.Lable.Authors + ",GROUP_CONCAT(DISTINCT "
					+ Category.Lable.Name + " SEPARATOR ',') as "
					+ Book.Lable.Categories + " FROM lms." + Book.EntityName
					+ " NATURAL LEFT JOIN lms." + Author.EntityName
					+ " NATURAL LEFT JOIN lms." + Category.EntityName
					+ " GROUP BY " + Book.Lable.ISBN + " HAVING "
					+ Book.Lable.ISBN + " = ?");
			stmt.setString(1, isbn);
			ResultSet rs = stmt.executeQuery();
			if (rs.first()) {
				result = new Book(rs);
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

	public static Vector<Book> getAllBooks(String fields) throws SQLException {
		Vector<Book> result = null;
		Connection conn = null;
		try {
			conn = Utilities.getSQLConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			ResultSet rs = conn.createStatement().executeQuery(
					"SELECT " + fields + " FROM lms." + Book.EntityName);
			result = new Vector<Book>();
			while (rs.next()) {
				result.addElement(new Book(rs));
			}
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return result;
	}

	public static Vector<Book> getAllBooks() throws SQLException {
		return getAllBooks("*");
	}

	public static Vector<Book> getAllBooksInRange(String fields, long start,
			long length) throws SQLException {
		Vector<Book> result = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = Utilities.getSQLConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			stmt = conn.prepareStatement("SELECT " + fields + " FROM lms."
					+ Book.EntityName + " LIMIT ?,?");
			stmt.setLong(1, start);
			stmt.setLong(2, length);
			ResultSet rs = stmt.executeQuery();
			result = new Vector<Book>();
			while (rs.next()) {
				result.addElement(new Book(rs));
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

	public static Vector<Book> getAllBooksInRange(long start, long length)
			throws SQLException {
		return getAllBooksInRange("*", start, length);
	}

	public static Vector<Book> getAllBooksWithAuthorsAndCatagoriesInRange(
			String fields, long start, long length) throws SQLException {
		Vector<Book> result = null;
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
					+ " FROM lms." + Book.EntityName
					+ " NATURAL LEFT JOIN lms." + Author.EntityName
					+ " NATURAL LEFT JOIN lms." + Category.EntityName
					+ " GROUP BY " + Book.Lable.ISBN + " LIMIT ?,?");
			stmt.setLong(1, start);
			stmt.setLong(2, length);
			ResultSet rs = stmt.executeQuery();
			result = new Vector<Book>();
			while (rs.next()) {
				result.addElement(new Book(rs));
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

	public static Vector<Book> getAllBooksWithAuthorsAndCatagoriesInRange(
			long start, long length) throws SQLException {
		return getAllBooksWithAuthorsAndCatagoriesInRange("*", start, length);
	}

	public static PreparedStatement getInsertStatement(Connection conn)
			throws SQLException {
		String statement = "INSERT INTO lms." + EntityName
				+ " VALUES( ? , ? , ? , ? , ? , ? , ? , ? )";
		PreparedStatement stmt = conn.prepareStatement(statement);
		return stmt;
	}

	public static Book getBookFromIndexForISBN(String isbn) {
		Book book = null;
		Index index = getIndex();
		Document bookDocument = index.get(isbn);
		book = new Book(bookDocument);
		return book;
	}

//	public static int deleteBookForISBNFromDatabase(String isbn) throws SQLException {
//		Book result = null;
//		Connection conn = null;
//		PreparedStatement stmt = null;
//		try {
//			conn = Utilities.getSQLConnection();
//			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//			stmt = conn.prepareStatement("SELECT * FROM lms." + Book.EntityName
//					+ " WHERE ISBN = ?");
//			stmt.setString(1, isbn);
//			ResultSet rs = stmt.executeQuery();
//			if (rs.first()) {
//				result = new Book(rs);
//			}
//		} finally {
//			if (stmt != null) {
//				stmt.close();
//			}
//			if (conn != null) {
//				conn.close();
//			}
//		}
//		return result;
//	}
	
	protected static void deleteBookFromIndexForId(String documentId,
			int retryCount) throws DeleteException {
		Index index = getIndex();
		try {
			index.delete(documentId);
		} catch (DeleteException e) {
			if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult()
					.getCode()) && retryCount < INDEX_DELETE_RETRY_LIMIT) {
				deleteBookFromIndexForId(documentId, retryCount + 1);
			} else {
				throw e;
			}
		}
	}

	public static void deleteBooksFromIndexForIds(String documentId)
			throws DeleteException {
		deleteBookFromIndexForId(documentId, 0);
	}

	protected static void deleteBooksFromIndexForIds(
			Iterable<String> documentIds, int retryCount)
			throws DeleteException {
		Index index = getIndex();
		try {
			index.delete(documentIds);
		} catch (DeleteException e) {
			if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult()
					.getCode()) && retryCount < INDEX_DELETE_RETRY_LIMIT) {
				deleteBooksFromIndexForIds(documentIds, retryCount + 1);
			} else {
				throw e;
			}
		}
	}

	public static void deleteBooksFromIndexForIds(Iterable<String> documentIds)
			throws DeleteException {
		deleteBooksFromIndexForIds(documentIds, 0);
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
	 */
	public Book(String isbn, String title, String publisher,
			String publicationYear, String edition, String description,
			String googleBooksID, int noOfPages, Author[] authors,
			Category[] categories) {
		super();
		this.isbn = isbn;
		this.title = title;
		this.publisher = publisher;
		this.publicationYear = publicationYear;
		this.edition = edition;
		this.description = description;
		this.googleBooksID = googleBooksID;
		this.noOfPages = noOfPages;
		this.authors = authors;
		this.categories = categories;
	}

	public Book(HttpServletRequest req) {
		this.isbn = req.getParameter("isbn");
		this.title = req.getParameter("title");
		this.publisher = req.getParameter("publisher");
		this.publicationYear = req.getParameter("publicationYear");
		this.edition = req.getParameter("edition");
		this.description = req.getParameter("description");
		this.googleBooksID = req.getParameter("googleBooksID");
		this.noOfPages = 0;
		try {
			this.noOfPages = Integer.parseInt(req.getParameter("noOfPages"));
		} catch (NumberFormatException e) {
		}

		String authorsString = req.getParameter("authors");
		if (authorsString != null) {
			this.authors = extractAuthors(authorsString);
		}
		String catagoriesString = req.getParameter("categories");
		if (catagoriesString != null) {
			this.categories = extractCatagories(catagoriesString);
		}
	}

	public Book(ResultSet rs) throws SQLException {
		this.isbn = rs.getString(Lable.ISBN.toString());
		try {
			this.title = rs.getString(Lable.Title.toString());
		} catch (SQLException e) {
			this.title = null;
		}
		try {
			this.publisher = rs.getString(Lable.Publisher.toString());
		} catch (SQLException e) {
			this.publisher = null;
		}
		try {
			this.publicationYear = rs.getString(Lable.PublicationYear
					.toString());
		} catch (SQLException e) {
			this.publicationYear = null;
		}
		try {
			this.edition = rs.getString(Lable.Edition.toString());
		} catch (SQLException e) {
			this.edition = null;
		}
		try {
			this.description = rs.getString(Lable.Description.toString());
		} catch (SQLException e) {
			this.description = null;
		}
		try {
			this.googleBooksID = rs.getString(Lable.GoogleBooksID.toString());
		} catch (SQLException e) {
			this.googleBooksID = null;
		}
		try {
			this.noOfPages = rs.getInt(Lable.NoOfPages.toString());
		} catch (SQLException e) {
			this.noOfPages = 0;
		}
		try {
			String authorsString = rs.getString(Lable.Authors.toString());
			if (authorsString != null) {
				this.authors = extractAuthors(authorsString);
			} else {
				this.authors = new Author[0];
			}
		} catch (SQLException e) {
			this.authors = null;
		}
		try {
			String catagoriesString = rs.getString(Lable.Categories.toString());
			if (catagoriesString != null) {
				this.categories = extractCatagories(catagoriesString);
			} else {
				this.categories = new Category[0];
			}
		} catch (SQLException e) {
			this.categories = null;
		}
	}

	public Book(Document bookDocument) {
		this.isbn = bookDocument.getId();
		try {
			this.title = bookDocument.getOnlyField(Book.Lable.Title.toString())
					.getText();
		} catch (IllegalArgumentException e) {
			this.title = null;
		}
		try {
			this.publisher = bookDocument.getOnlyField(
					Book.Lable.Publisher.toString()).getText();
		} catch (IllegalArgumentException e) {
			this.publisher = null;
		}
		try {
			this.edition = bookDocument.getOnlyField(
					Book.Lable.Edition.toString()).getText();
		} catch (IllegalArgumentException e) {
			this.edition = null;
		}
		try {
			this.publicationYear = bookDocument.getOnlyField(
					Book.Lable.PublicationYear.toString()).getText();
		} catch (IllegalArgumentException e) {
			this.publicationYear = null;
		}
		try {
			this.googleBooksID = bookDocument.getOnlyField(
					Book.Lable.GoogleBooksID.toString()).getAtom();
		} catch (IllegalArgumentException e) {
			this.googleBooksID = null;
		}
		try {
			this.noOfPages = bookDocument
					.getOnlyField(Book.Lable.NoOfPages.toString()).getNumber()
					.intValue();
		} catch (IllegalArgumentException e) {
			this.publisher = null;
		}
		try {
			this.description = bookDocument.getOnlyField(
					Book.Lable.Description.toString()).getText();
		} catch (IllegalArgumentException e) {
			this.description = null;
		}
		Iterable<Field> authorsFields = bookDocument
				.getFields(Book.Lable.Authors.toString());
		if (authorsFields != null) {
			this.authors = new Author[0];
			Vector<Author> authorsVector = new Vector<Author>();
			for (Field authorField : authorsFields) {
				authorsVector
						.addElement(new Author(authorField.getText(), this));
			}
			this.authors = authorsVector.toArray(this.authors);
		} else {
			this.authors = null;
		}
		Iterable<Field> categoriesFields = bookDocument
				.getFields(Book.Lable.Categories.toString());
		if (categoriesFields != null) {
			this.categories = new Category[0];
			Vector<Category> categoriesVector = new Vector<Category>();
			for (Field categoryField : categoriesFields) {
				categoriesVector.addElement(new Category(categoryField
						.getText(), this));
			}
			this.categories = categoriesVector.toArray(this.categories);
		} else {
			this.categories = null;
		}
	}

	/* Member Function */

	/**
	 * 
	 * @param authorsString
	 * @return
	 */
	private Author[] extractAuthors(String authorsString) {
		if (authorsString.equals("")) {
			return new Author[0];
		}
		String authorsArr[] = authorsString.split(",");
		Author authors[] = new Author[authorsArr.length];
		for (int i = 0; i < authorsArr.length; i++) {
			authors[i] = new Author(authorsArr[i].trim(), this);
		}
		return authors;
	}

	private Category[] extractCatagories(String catagoriesString) {
		if (catagoriesString.equals("")) {
			return new Category[0];
		}
		String catagoriesArr[] = catagoriesString.split(",");
		Category catagories[] = new Category[catagoriesArr.length];
		for (int i = 0; i < catagoriesArr.length; i++) {
			catagories[i] = new Category(catagoriesArr[i].trim(), this);
		}
		return catagories;
	}

	protected PutResponse addToIndex(int retryCount) throws PutException {
		PutResponse putResponse = null;
		Builder documentBuilder = Document
				.newBuilder()
				.setId(this.isbn)
				.addField(
						Field.newBuilder().setName(Book.Lable.ISBN.toString())
								.setText(this.getISBN()))
				.addField(
						Field.newBuilder().setName(Book.Lable.Title.toString())
								.setText(this.getTitle()))
				.addField(
						Field.newBuilder()
								.setName(Book.Lable.Publisher.toString())
								.setText(this.getPublisher()))
				.addField(
						Field.newBuilder()
								.setName(Book.Lable.Edition.toString())
								.setText(this.getEdition()))
				.addField(
						Field.newBuilder()
								.setName(Book.Lable.PublicationYear.toString())
								.setText(this.getPublicationYear()))
				.addField(
						Field.newBuilder()
								.setName(Book.Lable.GoogleBooksID.toString())
								.setAtom(this.getGoogleBooksID()))
				.addField(
						Field.newBuilder()
								.setName(Book.Lable.NoOfPages.toString())
								.setNumber(this.getNoOfPages()))
				.addField(
						Field.newBuilder()
								.setName(Book.Lable.Description.toString())
								.setHTML(this.getDescription()));
		Author authors[];
		try {
			authors = this.getAuthors();
			for (int i = 0; i < authors.length; i++) {
				Author author = authors[i];
				documentBuilder.addField(Field.newBuilder()
						.setName(Book.Lable.Authors.toString())
						.setText(author.getName()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Category categories[];
		try {
			categories = this.getCategories();
			for (int i = 0; i < categories.length; i++) {
				Category category = categories[i];
				documentBuilder.addField(Field.newBuilder()
						.setName(Book.Lable.Categories.toString())
						.setText(category.getName()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Document bookDocument = documentBuilder.build();
		Index index = Book.getIndex();
		try {
			putResponse = index.put(bookDocument);
		} catch (PutException e) {
			if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult()
					.getCode()) && retryCount < INDEX_PUT_RETRY_LIMIT) {
				this.addToIndex(retryCount + 1);
			} else {
				throw e;
			}
		}
		return putResponse;
	}

	public PutResponse addToIndex() throws PutException {
		return addToIndex(0);
	}

	public int insertBookIntoDataBaseUsingPreparedStatement(
			PreparedStatement stmt) throws SQLException {
		int success = 2;
		stmt.setString(1, this.isbn);
		stmt.setString(2, this.title);
		stmt.setString(3, this.publisher);
		stmt.setString(4, this.edition);
		stmt.setString(5, this.publicationYear);
		stmt.setString(6, this.googleBooksID);
		stmt.setInt(7, this.noOfPages);
		stmt.setString(8, this.description);
		success = stmt.executeUpdate();
		return success;
	}

	public int insertIntoDataBaseUsingConnection(Connection conn)
			throws SQLException {
		int success = 2;
		PreparedStatement stmt = null;
		try {
			stmt = getInsertStatement(conn);
			success = insertBookIntoDataBaseUsingPreparedStatement(stmt);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return success;
	}

	public int[] insertAllAuthorsIntoDataBaseUsingPreparedStatement(
			PreparedStatement batchStmt) throws SQLException {
		int statuses[] = null;
		for (int i = 0; i < this.authors.length; i++) {
			this.authors[i].insertIntoDataBaseUsingBatch(batchStmt);
		}
		statuses = batchStmt.executeBatch();
		return statuses;
	}

	public int[] insertAllAuthorsIntoDataBaseUsingConnection(Connection conn)
			throws SQLException {
		int statuses[] = null;
		PreparedStatement batchStmt = null;
		try {
			batchStmt = Author.getInsertStatement(conn);
			statuses = insertAllAuthorsIntoDataBaseUsingPreparedStatement(batchStmt);
		} finally {
			if (batchStmt != null) {
				batchStmt.close();
			}
		}
		return statuses;
	}

	public int[] insertAllCatagoriesIntoDataBaseUsingPreparedStatement(
			PreparedStatement batchStmt) throws SQLException {
		int statuses[] = null;
		for (int i = 0; i < this.categories.length; i++) {
			this.categories[i].insertIntoDataBaseUsingBatch(batchStmt);
		}
		statuses = batchStmt.executeBatch();
		return statuses;
	}

	public int[] insertAllCatagoriesIntoDataBaseUsingConnection(Connection conn)
			throws SQLException {
		int statuses[] = null;
		PreparedStatement batchStmt = null;
		try {
			batchStmt = Category.getInsertStatement(conn);
			statuses = insertAllCatagoriesIntoDataBaseUsingPreparedStatement(batchStmt);
		} finally {
			if (batchStmt != null) {
				batchStmt.close();
			}
		}
		return statuses;
	}

	/* Getter/Setters */

	/**
	 * @return the ISBN
	 */
	public String getISBN() {
		return isbn;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the publisher
	 */
	public String getPublisher() {
		return publisher;
	}

	/**
	 * @return the publicationYear
	 */
	public String getPublicationYear() {
		return publicationYear;
	}

	/**
	 * @return the edition
	 */
	public String getEdition() {
		return edition;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the googleBooksID
	 */
	public String getGoogleBooksID() {
		return googleBooksID;
	}

	/**
	 * @return the noOfPages
	 */
	public int getNoOfPages() {
		return noOfPages;
	}

	/**
	 * @return the authors
	 * @throws SQLException
	 */
	public Author[] getAuthors() throws SQLException {
		if (authors == null) {
			authors = new Author[0];
			authors = Author.getAuthorsForBook(this).toArray(authors);
		}
		return authors;
	}

	/**
	 * @return the categories
	 * @throws SQLException
	 */
	public Category[] getCategories() throws SQLException {
		if (categories == null) {
			categories = new Category[0];
			categories = Category.getCatagoriesForBook(this)
					.toArray(categories);
		}
		return categories;
	}
}
