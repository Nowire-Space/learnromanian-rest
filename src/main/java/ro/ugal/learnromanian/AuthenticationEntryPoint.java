package ro.ugal.learnromanian;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response,
//                         AuthenticationException authException) throws IOException {
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");
//        PrintWriter writer = response.getWriter();
//        writer.println("HTTP Status 401 : " + authException.getMessage());
//    }
//
//    @Override
//    public void afterPropertiesSet() {
//        setRealmName("Auth_Realm");
//        super.afterPropertiesSet();
//    }
}
