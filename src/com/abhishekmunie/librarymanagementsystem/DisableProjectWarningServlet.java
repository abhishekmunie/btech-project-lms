package com.abhishekmunie.librarymanagementsystem;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class DisableProjectWarningServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		session.setAttribute("no-project-warning", true);
		String redirect = req.getParameter("redirect");
		if (redirect != null) {
			if (redirect.charAt(0) != '/') {
				redirect = "/" + redirect;
			}
			resp.sendRedirect(redirect);
		} else {
			resp.sendRedirect("/");
		}

	}
}
