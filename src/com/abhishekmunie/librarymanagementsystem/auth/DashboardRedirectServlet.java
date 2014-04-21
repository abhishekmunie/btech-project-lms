package com.abhishekmunie.librarymanagementsystem.auth;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.abhishekmunie.librarymanagementsystem.cl.ChiefLibrarian;
import com.abhishekmunie.librarymanagementsystem.staff.Staff;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class DashboardRedirectServlet extends HttpServlet {

	private ServletConfig servletConfig;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (userService.isUserAdmin()) {
			resp.sendRedirect(req.getRequestURI().replaceFirst("^/dashboard",
					"/admin"));
			return;
		} else if (user != null) {
			try {
				if (ChiefLibrarian.isCurrentUserChiefLibrarian()) {
					resp.sendRedirect(req.getRequestURI().replaceFirst(
							"^/dashboard", "/cl"));
					return;
				} else if (Staff.isCurrentUserStaff()) {
					resp.sendRedirect(req.getRequestURI().replaceFirst(
							"^/dashboard", "/staff"));
					return;
				} else {
					resp.sendRedirect(req.getRequestURI().replaceFirst(
							"^/dashboard", "/user"));
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		resp.sendRedirect("/");
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		this.servletConfig = servletConfig;
	}
}
