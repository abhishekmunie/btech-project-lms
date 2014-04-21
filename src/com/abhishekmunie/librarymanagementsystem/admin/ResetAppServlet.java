package com.abhishekmunie.librarymanagementsystem.admin;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.abhishekmunie.librarymanagementsystem.AppConfig;
import com.abhishekmunie.librarymanagementsystem.Utilities;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class ResetAppServlet extends HttpServlet {

	private ServletConfig servletConfig;
	private String jspURL = "/admin/reset/index.jsp";

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("isForwordedRequest", true);
		servletConfig.getServletContext().getRequestDispatcher(jspURL)
				.forward(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		UserService userService = UserServiceFactory.getUserService();
		if (!userService.isUserAdmin()) {
			resp.sendRedirect("/");
		}
		req.setAttribute("isForwordedRequest", true);
		String confirmation = req.getParameter("confirmation");
		if (confirmation.equals("Reset")) {
			try {
				InputStream initScriptInputStream = servletConfig.getServletContext().getResourceAsStream("/WEB-INF/init.sql");
				AppConfig.resetApp(initScriptInputStream);
				req.setAttribute("successmsg", "App was reset successfully.");
				req.getRequestDispatcher(jspURL).forward(req, resp);
			} catch (SQLException e) {
				e.printStackTrace();
				req.setAttribute("errormsg", "Couldn't reset app.");
				req.setAttribute("errorex", e);
				req.getRequestDispatcher(jspURL).forward(req, resp);
			}
		} else {
			req.setAttribute("errormsg", "Incorrect confirmation text.");
			Utilities.setParametersAsAttributes(req);
			req.getRequestDispatcher(jspURL).forward(req, resp);
		}
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		this.servletConfig = servletConfig;
	}
}
