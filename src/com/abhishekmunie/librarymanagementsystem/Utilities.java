package com.abhishekmunie.librarymanagementsystem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.google.api.client.util.Joiner;
import com.google.appengine.api.utils.SystemProperty;

public class Utilities {

	private static final Logger log = Logger.getLogger(Utilities.class.getName());

	public static Connection getSQLConnection() throws SQLException {
    String url = null;
    try {
      if (SystemProperty.environment.value() ==
          SystemProperty.Environment.Value.Production) {
        // Load the class that provides the new "jdbc:google:mysql://" prefix.
        Class.forName("com.mysql.jdbc.GoogleDriver");
        url = CONFIG.CloudSQLURL;
      } else {
        Class.forName("com.mysql.jdbc.Driver");
        url = CONFIG.MySQLURL;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
		return DriverManager.getConnection(url);
	}

	public static String urlEncodeUTF8(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	public static String urlEncodeUTF8(Map<?, ?> map) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			if (sb.length() > 0) {
				sb.append("&");
			}
			sb.append(String.format("%s=%s", urlEncodeUTF8(entry.getKey()
					.toString()), urlEncodeUTF8(entry.getValue().toString())));
		}
		return sb.toString();
	}

	public static String urlEncodeUTF8ParameterMap(HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		Map<String, String[]> parameterMap = req.getParameterMap();
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			if (sb.length() > 0) {
				sb.append("&");
			}
			sb.append(String.format("%s=%s", urlEncodeUTF8(entry.getKey()),
					urlEncodeUTF8(entry.getValue()[0])));
		}
		return sb.toString();
	}

	public static String setParametersAsAttributes(HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		Map<String, String[]> parameterMap = req.getParameterMap();
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			req.setAttribute(entry.getKey(), entry.getValue()[0]);
		}
		return sb.toString();
	}

	public static final String ISBN10_MAYBE_REGEX = "/^(?:[0-9]{9}X|[0-9]{10})$/";
	public static final String ISBN13_MAYBE_REGEX = "/^(?:[0-9]{13})$/";

	public static boolean isValidISBN10(String isbn10String) {
		int checksum = 0, i;
		String sanitizedISBN10String = isbn10String
				.replaceAll("/[\\s-]+/g", "");
		if (!sanitizedISBN10String.matches(ISBN10_MAYBE_REGEX)) {
			return false;
		}
		for (i = 0; i < 9; i++) {
			checksum += (i + 1) * sanitizedISBN10String.charAt(i);
		}
		if (sanitizedISBN10String.charAt(9) == 'X') {
			checksum += 10 * 10;
		} else {
			checksum += 10 * sanitizedISBN10String.charAt(9);
		}
		if ((checksum % 11) == 0) {
			return true;
		}
		return false;
	}

	public static boolean isValidISBN13(String isbn13String) {
		int checksum = 0, i;
		String sanitizedISBN13String = isbn13String
				.replaceAll("/[\\s-]+/g", "");
		if (!sanitizedISBN13String.matches(ISBN13_MAYBE_REGEX)) {
			return false;
		}
		int factor[] = { 1, 3 };
		for (i = 0; i < 12; i++) {
			checksum += factor[i % 2] * sanitizedISBN13String.charAt(i);
		}
		if (sanitizedISBN13String.charAt(12) - ((10 - (checksum % 10)) % 10) == 0) {
			return true;
		}
		return false;
	}

	public static boolean isValidISBN(String isbnString) {
		// return isValidISBN10(isbnString) || isValidISBN13(isbnString);
		return true;
	}

	public static String join(List<String> stringList, char separator) {
		return Joiner.on(separator).join(stringList);
//		return "";
	}

	public static String join(List<String> stringList) {
		return join(stringList, ',');
//		return "";
	}
}
