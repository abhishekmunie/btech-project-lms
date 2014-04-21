package com.abhishekmunie.librarymanagementsystem.cl;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author abhishekmunie
 * 
 */
public class CLFilterImpl implements Filter {

	private FilterConfig filterConfig;

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		try {
			if (ChiefLibrarian.isCurrentUserChiefLibrarian()) {
				filterChain.doFilter(request, response);
				return;
			} else if (this.filterConfig != null) {
				getFilterConfig().getServletContext()
						.getRequestDispatcher("/auth/login/cl")
						.include(request, response);
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public FilterConfig getFilterConfig() {
		return filterConfig;
	}

	public void init(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	public void destroy() {
	}
}
