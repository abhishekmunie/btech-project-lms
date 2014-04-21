package com.abhishekmunie.librarymanagementsystem.books;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.abhishekmunie.librarymanagementsystem.Utilities;
import com.abhishekmunie.librarymanagementsystem.staff.Staff;

@SuppressWarnings("serial")
public class IssueServlet extends HttpServlet {

	private ServletConfig servletConfig;
	private String jspURL = "/staff/books/issue/index.jsp";

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
		Issue issue = new Issue(req);
		req.setAttribute("isForwordedRequest", true);
		try {
			issue.addIssueIntoDatabase();
			req.setAttribute("successmsg", "The book was issued successfully.");
			req.getRequestDispatcher(jspURL).forward(req, resp);
		} catch (SQLException e) {
			e.printStackTrace();
			req.setAttribute("errormsg", "An error occured while issuing the book.");
			req.setAttribute("errorex", e);
			Utilities.setParametersAsAttributes(req);
			req.setAttribute("autoFillUsingAttribute", true);
			req.getRequestDispatcher(jspURL).forward(req, resp);
		}
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		this.servletConfig = servletConfig;
	}
}
