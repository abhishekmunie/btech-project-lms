package com.abhishekmunie.librarymanagementsystem.cl;

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
public class StaffManagementServlet extends HttpServlet {

	private ServletConfig servletConfig;
	private String jspURL = "/cl/staff-management/index.jsp";

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
		String action = req.getParameter("action");
		String emailid = req.getParameter("emailid");
		req.setAttribute("isForwordedRequest", true);
		try {
			switch (action) {
			case "add":
				Staff.addStaffIntoDatabaseWithEmail(emailid);
				break;
			case "remove":
			case "delete":
				Staff.deleteStaffIntoDatabaseWithEmail(emailid);
				break;
			default:
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Bad Request");
				req.setAttribute("successmsg", "");
				return;
				// break;
			}
			req.setAttribute("successmsg", "Staff member with email " + emailid
					+ " was successfully " + action + ".");
			req.getRequestDispatcher(jspURL).forward(req, resp);
		} catch (SQLException e) {
			e.printStackTrace();
			req.setAttribute("errormsg", "Unable to " + action
					+ " staff member with email " + emailid + ".");
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
