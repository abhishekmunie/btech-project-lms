package com.abhishekmunie.librarymanagementsystem.auth;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class LogoutServlet  extends HttpServlet {
	@Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
              throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        resp.sendRedirect(userService.createLogoutURL(userService.createLoginURL("/")));
    }
}
