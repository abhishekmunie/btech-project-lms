package com.abhishekmunie.librarymanagementsystem.books;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.abhishekmunie.librarymanagementsystem.CONFIG;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volume.VolumeInfo;
import com.google.api.services.books.model.Volume.VolumeInfo.IndustryIdentifiers;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe;

public class GoogleBooks {

	public static Volume getGoogleBookVolumeByID(String id, HttpServletRequest req)
			throws GeneralSecurityException, IOException {
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
		// // Set up Books client.
		// Books books = new Books.Builder(
		// GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null)
		// .setApplicationName(CONFIG.APPLICATION_NAME)
		// .setGoogleClientRequestInitializer(
		// new BooksRequestInitializer()).build();
		// UserService userService = UserServiceFactory.getUserService();
		// User user = userService.getCurrentUser();
		// Credential credential = authFlow.loadCredential(user.getUserId());
		// Build the Books object using the credentials
		// Books books = new Books.Builder(
		// GoogleAPIUtils.HTTP_TRANSPORT, GoogleAPIUtils.JSON_FACTORY, null)
		// .setApplicationName(CONFIG.APPLICATION_NAME)
		// .build();
//		JsonHttpRequestInitializer initializer = new GoogleKeyInitializer();
//	    Plus plus = Plus.builder(new NetHttpTransport(), new JacksonFactory())
//	            .setApplicationName("Google-PlusSample/1.0")
//	            .setJsonHttpRequestInitializer(initializer)
//	            .build();
	    HttpRequestInitializer hri = (HttpRequestInitializer) new BooksRequestInitializer();
		final Books books = new Books.Builder(
				GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null)
				.setApplicationName("vit-lms/1.0")
				.setGoogleClientRequestInitializer(new BooksRequestInitializer(CONFIG.API_KEY))
				.setBooksRequestInitializer(
						new BooksRequestInitializer(CONFIG.API_KEY))
				.build();

		Books.Volumes.Get volumeGet = books.volumes().get(id);
		// volumesList.setFilter("ebooks");
		Volume volume = volumeGet.execute();
		return volume;
	}

	public static String getISBNFromVolumeInfo(VolumeInfo volumeInfo) {
		List<IndustryIdentifiers> industryIdentifiersList = volumeInfo
				.getIndustryIdentifiers();
		String ISBN = null;
		if (industryIdentifiersList != null) {
			for (IndustryIdentifiers industryIdentifiers : industryIdentifiersList) {
				if (industryIdentifiers.getType().equals("ISBN_13")) {
					return industryIdentifiers.getIdentifier();
				} else if (industryIdentifiers.getType().equals("ISBN_10")) {
					ISBN = industryIdentifiers.getIdentifier();
					;
				}
			}
		}
		return ISBN;
	}

	public static String getISBN13FromVolumeInfo(VolumeInfo volumeInfo) {
		List<IndustryIdentifiers> industryIdentifiersList = volumeInfo
				.getIndustryIdentifiers();
		if (industryIdentifiersList != null) {
			for (IndustryIdentifiers industryIdentifiers : industryIdentifiersList) {
				if (industryIdentifiers.getType().equals("ISBN_13")) {
					return industryIdentifiers.getIdentifier();
				}
			}
		}
		return null;
	}
}
