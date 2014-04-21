package com.abhishekmunie.librarymanagementsystem.books;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.sql.*;

import com.abhishekmunie.librarymanagementsystem.Utilities;
import com.google.appengine.api.search.PutResponse;
import com.google.appengine.api.search.DeleteException;

/**
 * 
 * @author abhishekmunie
 */
@SuppressWarnings("serial")
public class AddServlet extends HttpServlet {

	private ServletConfig servletConfig;

	private String jspURL = "/staff/books/add/index.jsp";

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
		Connection conn = null;
		String msg = null;
		PutResponse putResponse = null;
		AvailableBook availableBook = new AvailableBook(req);
		req.setAttribute("isForwordedRequest", true);
		try {
			conn = Utilities.getSQLConnection();
			conn.setAutoCommit(false);
			msg = "An Error occured while the Adding the Book.";
			availableBook.insertIntoDataBaseUsingConnection(conn);
			msg = "An Error occured while the Adding Book's Authors";
			availableBook.insertAllAuthorsIntoDataBaseUsingConnection(conn);
			msg = "An Error occured while the Adding Catagories's Authors";
			availableBook.insertAllCatagoriesIntoDataBaseUsingConnection(conn);
			msg = "An Error occured while the Adding Book to Index";
			putResponse = availableBook.addToIndex();
			msg = null;
			conn.commit();
			req.setAttribute("successmsg", "");
			req.getRequestDispatcher(jspURL).forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (conn != null) {
					conn.rollback();
				}
				if (putResponse != null) {
					Book.deleteBooksFromIndexForIds(putResponse.getIds());
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			} catch (DeleteException ex) {
				ex.printStackTrace();
			}
			req.setAttribute("errormsg", msg);
			req.setAttribute("errorex", e);
			Utilities.setParametersAsAttributes(req);
			req.setAttribute("autoFillUsingAttribute", true);
			req.getRequestDispatcher(jspURL).forward(req, resp);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		this.servletConfig = servletConfig;
		// this.myParam = servletConfig.getInitParameter("myParam");
	}
}
