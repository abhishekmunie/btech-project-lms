package com.abhishekmunie.librarymanagementsystem.books;

/*
 * Copyright (c) 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import com.abhishekmunie.librarymanagementsystem.GoogleAPIUtils;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;
import com.google.api.services.books.Books;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Entry servlet for the Books API App Engine Sample.
 * Demonstrates how to make an authenticated API call using OAuth 2 helper classes.
 */
public class GoogleBooksAuthorizationCodeServlet
    extends AbstractAppEngineAuthorizationCodeServlet {

  /**
   * Be sure to specify the name of your application. If the application name is {@code null} or
   * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
   */
  private static final String APPLICATION_NAME = "vit-lms/1.0";

  private static final long serialVersionUID = 1L;

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    // Get the stored credentials using the Authorization Flow
    AuthorizationCodeFlow authFlow = initializeFlow();
    Credential credential = authFlow.loadCredential(getUserId(req));
    // Build the Books object using the credentials
    @SuppressWarnings("unused")
    Books books = new Books.Builder(
    		GoogleAPIUtils.HTTP_TRANSPORT, GoogleAPIUtils.JSON_FACTORY, credential)
        .setApplicationName(APPLICATION_NAME)
        .build();

    // Add the code to make an API call here.

    // Send the results as the response
    resp.setStatus(200);
    resp.setContentType("text/html");
    PrintWriter writer = resp.getWriter();
    writer.println("Success! Now add code here.");
  }
  @Override
  protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
    return GoogleAPIUtils.initializeFlow();
  }

  @Override
  protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
    return GoogleAPIUtils.getRedirectUri(req);
  }
}
