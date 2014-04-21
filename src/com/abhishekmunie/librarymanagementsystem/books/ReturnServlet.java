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
public class ReturnServlet extends HttpServlet {

	private ServletConfig servletConfig;
	private String jspURL = "/staff/books/return/index.jsp";

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
		req.setAttribute("isForwordedRequest", true);
		try {
			int issueID = Integer.parseInt(req.getParameter("issueID"));
			Issue.addReturnDateIntoDatabaseForIssueID(issueID);
			req.setAttribute("successmsg", "Book was returned successfully.");
			req.getRequestDispatcher(
					jspURL + "?emailID=" + req.getParameter("emailID"))
					.forward(req, resp);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			req.setAttribute("errormsg", "Invalid Issue ID.");
			req.setAttribute("errorex", e);
		} catch (SQLException e) {
			e.printStackTrace();
			req.setAttribute("errormsg", "Unable to return the requested book.");
			req.setAttribute("errorex", e);
		}
		req.getRequestDispatcher(
				jspURL + "?emailID=" + req.getParameter("emailID"))
				.forward(req, resp);
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		this.servletConfig = servletConfig;
	}
}
