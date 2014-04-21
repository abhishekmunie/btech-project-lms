package com.abhishekmunie.librarymanagementsystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import com.abhishekmunie.librarymanagementsystem.books.Book;
import com.abhishekmunie.librarymanagementsystem.books.Search;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Index;

public class AppConfig {

	public static void resetApp(InputStream initScriptInputStream) throws IOException, SQLException {
		Index index = Book.getIndex();
		List<Document> documents = Search.getAllBookDocuments();
		Vector<String> documentIds = new Vector<>(documents.size());
		for (Document document : documents) {
			documentIds.addElement(document.getId());
		}
		index.delete(documentIds);
		Connection conn = null;
		try {
			conn = Utilities.getSQLConnection();
			ScriptRunner scriptRunner = new ScriptRunner(conn, false, false);
			InputStreamReader reader = new InputStreamReader(initScriptInputStream);
			scriptRunner.runScript(reader);
			reader.close();
			/*
			String resetStatements[] = {
			"TRUNCATE TABLE `lms`.`Author`",
			"TRUNCATE TABLE `lms`.`Category`",
			"TRUNCATE TABLE `lms`.`Reissue`",
			"TRUNCATE TABLE `lms`.`Issue`",
			"TRUNCATE TABLE `lms`.`BookAvailabilityNotification`",
			"TRUNCATE TABLE `lms`.`AvailableBook`",
			"TRUNCATE TABLE `lms`.`ApprovedBook`",
			"TRUNCATE TABLE `lms`.`BookRequest`",
			"TRUNCATE TABLE `lms`.`RequestedBook`",
			"TRUNCATE TABLE `lms`.`Book`",
			"TRUNCATE TABLE `lms`.`Staff`" };
			for (int i = 0; i < resetStatements.length; i++) {
				String statement = resetStatements[i];
				conn.createStatement().executeUpdate(statement);
			}*/
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
}
