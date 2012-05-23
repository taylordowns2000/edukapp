package uk.ac.edukapp.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CorsFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		((HttpServletResponse)response).addHeader("Access-Control-Allow-Origin", "*");
		chain.doFilter(arg0, response);

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		

	}

}
