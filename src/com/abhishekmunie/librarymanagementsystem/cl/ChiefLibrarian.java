package com.abhishekmunie.librarymanagementsystem.cl;

import java.sql.SQLException;

import com.abhishekmunie.librarymanagementsystem.CONFIG;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class ChiefLibrarian extends
		com.abhishekmunie.librarymanagementsystem.user.User {

	public static boolean isChiefLibrarian(String email) throws SQLException {
		return (email.equals("cl.lms.vit@gmail.com") || email.equals(CONFIG.CL_EMAIL));
	}

	public static boolean isCurrentUserChiefLibrarian() throws SQLException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		return isChiefLibrarian(user.getEmail());
	}
}
