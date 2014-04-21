package com.abhishekmunie.librarymanagementsystem.books;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import com.abhishekmunie.librarymanagementsystem.staff.Staff.Lable;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.FieldExpression;
import com.google.appengine.api.search.GetRequest;
import com.google.appengine.api.search.GetResponse;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchException;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.SortExpression;
import com.google.appengine.api.search.SortOptions;
import com.google.appengine.api.search.StatusCode;

public class Search {

	public static final int INDEX_SEARCH_RETRY_LIMIT = 3;

	public static Book getBookForISBN(String isbn) {
		return Book.getBookFromIndexForISBN(isbn);
	}

	protected static Results<ScoredDocument> getScoredBookResultsForSearchQuery(
			String queryString, int retryCount) throws SearchException {
		Results<ScoredDocument> results = null;
		try {
			Index index = Book.getIndex();
			results = index.search(queryString);
		} catch (SearchException e) {
			if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult()
					.getCode()) && retryCount < INDEX_SEARCH_RETRY_LIMIT) {
				getScoredBookResultsForSearchQuery(queryString, retryCount + 1);
			} else {
				throw e;
			}
		}
		return results;
	}

	public static Results<ScoredDocument> getScoredBookResultsForSearchQuery(
			String queryString) throws SearchException {
		return getScoredBookResultsForSearchQuery(queryString, 0);
	}

	public static Book[] getBooksForSearchQuery(String queryString)
			throws SearchException {
		Book books[] = new Book[0];
		Results<ScoredDocument> results = getScoredBookResultsForSearchQuery(queryString);
		Vector<Book> booksVector = new Vector<Book>();
		for (ScoredDocument scoredBookDocument : results) {
			booksVector.addElement(new Book(scoredBookDocument));
		}
		books = booksVector.toArray(books);
		return books;
	}

	private static String addToQuery(String query, String lable) {
		if (query != null && !query.trim().equals("")) {
			return " " + lable + ": " + query;
		}
		return "";
	}

	public static Book[] getBooksForSearchFields(String query, String isbn,
			String title, String publisher, String publicationYear,
			String edition, String description, String googleBooksID,
			String concatinatedAuthorsString,
			String concatinatedCategoriesString) {
		String builtQueryString = (query != null) ? query : "";

		builtQueryString += addToQuery(title, Book.Lable.Title.toString());
		builtQueryString += addToQuery(isbn, Book.Lable.ISBN.toString());
		builtQueryString += addToQuery(publisher,
				Book.Lable.Publisher.toString());
		builtQueryString += addToQuery(publicationYear,
				Book.Lable.PublicationYear.toString());
		builtQueryString += addToQuery(edition, Book.Lable.Edition.toString());
		builtQueryString += addToQuery(googleBooksID,
				Book.Lable.GoogleBooksID.toString());
		builtQueryString += addToQuery(description,
				Book.Lable.Description.toString());

		if (concatinatedAuthorsString != null
				&& !concatinatedAuthorsString.trim().equals("")) {
			String authorsArr[] = concatinatedAuthorsString.split(",");
			builtQueryString += " " + Book.Lable.Authors + ":";
			for (int i = 0; i < authorsArr.length; i++) {
				builtQueryString += " " + authorsArr[i].trim();
			}
		}

		if (concatinatedCategoriesString != null
				&& !concatinatedCategoriesString.trim().equals("")) {
			String categoriesArr[] = concatinatedCategoriesString.split(",");
			builtQueryString += " " + Book.Lable.Authors + ":";
			for (int i = 0; i < categoriesArr.length; i++) {
				builtQueryString += " " + categoriesArr[i].trim();
			}
		}

		try {
			// Build the SortOptions with 2 sort keys
			SortOptions sortOptions = SortOptions
					.newBuilder()
					.addSortExpression(
							SortExpression
									.newBuilder()
									.setExpression(Book.Lable.Title.toString())
									.setDirection(
											SortExpression.SortDirection.ASCENDING)
									.setDefaultValue("")).setLimit(100).build();

			// Build the QueryOptions
			QueryOptions.Builder queryOptionsBuilder = QueryOptions
					.newBuilder().setLimit(10).setSortOptions(sortOptions);
			QueryOptions options = queryOptionsBuilder.build();

			// Build the Query and run the search
			Query q = Query.newBuilder().setOptions(options)
					.build(builtQueryString);
			// A query string
			// String queryString = "product: piano AND price < 5000";

			Index index = Book.getIndex();
			Book books[] = new Book[0];
			Results<ScoredDocument> results = index.search(q);
			Vector<Book> booksVector = new Vector<Book>();
			for (ScoredDocument scoredBookDocument : results) {
				booksVector.addElement(new Book(scoredBookDocument));
			}
			books = booksVector.toArray(books);
			return books;
		} catch (SearchException e) {
			// handle exception...
		}
		return null;
		// return getBooksForSearchQuery(query);
	}

	public static List<Document> getAllBookDocuments() {
		Index index = Book.getIndex();
		GetResponse<Document> response = index.getRange(GetRequest.newBuilder()
				.build());
		List<Document> documents = response.getResults();
		return documents;
	}

	public static Collection<ScoredDocument> getAllBookDocumentsBySearch() {
		Index index = Book.getIndex();
		// GetResponse<Document> response =
		// index.getRange(GetRequest.newBuilder().build());
		Collection<ScoredDocument> documents = index.search("").getResults();
		return documents;
	}
}
