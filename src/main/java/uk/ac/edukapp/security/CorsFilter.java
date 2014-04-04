package uk.ac.edukapp.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CorsFilter implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		String method = ((HttpServletRequest)request).getMethod();
		if ( "OPTIONS".equalsIgnoreCase(method)) {
			((HttpServletResponse)response).addHeader("Access-Control-Allow-Methods", "OPTIONS");
			((HttpServletResponse)response).addHeader("Access-Control-Allow-Credentials", "true");
		}
		chain.doFilter(request, response);

	}

	public void init(FilterConfig arg0) throws ServletException {
		

	}

}
