package uk.ac.edukapp.util;

import javax.servlet.http.Cookie;

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

}
