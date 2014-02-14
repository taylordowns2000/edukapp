package uk.ac.edukapp.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class ServletUtils {

	public static String getCookieValue(Cookie[] cookies, String cookieName,
			String defaultValue) {
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookieName.equals(cookie.getName()))
				return (cookie.getValue());
		}
		return (defaultValue);
	}

	public static String getServletRootURL ( HttpServletRequest request ) {
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		String protocol;
		
		if (request.isSecure()){
			protocol = "https";
		}
		else {
			protocol = "http";
		}
		
		String root = protocol+"://"+serverName+":"+serverPort+"/edukapp";
		
		return root;
	}
	
	
	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(.\\d+)?");
	}
}
