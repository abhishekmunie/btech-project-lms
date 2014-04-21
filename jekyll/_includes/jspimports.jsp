<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="com.abhishekmunie.librarymanagementsystem.Utilities"%>
{% for property in page.jsp_imports %}<%@ {{ property[1] }} import="{{ property[0] }}"%>
{% endfor %}
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
