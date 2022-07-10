package ro.ugal.learnromanian.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class ApplicationRequestLogger extends OncePerRequestFilter {

    private static final Logger requestLogger = LoggerFactory.getLogger(ApplicationRequestLogger.class);

    private static final String LOCALHOST_IPV4 = "127.0.0.1";
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
    private static final String UNKNOWN_STRING = "unknown";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String ipAddress = request.getHeader("X-Forwarded-For");
        if(StringUtils.isEmpty(ipAddress) || UNKNOWN_STRING.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if(StringUtils.isEmpty(ipAddress) || UNKNOWN_STRING.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if(StringUtils.isEmpty(ipAddress) || UNKNOWN_STRING.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(LOCALHOST_IPV4.equals(ipAddress) || LOCALHOST_IPV6.equals(ipAddress)) {
                try {
                    InetAddress inetAddress = InetAddress.getLocalHost();
                    ipAddress = inetAddress.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }

        requestLogger.info("Request from {} for URL {}", ipAddress, request.getRequestURI());

        filterChain.doFilter(request, response);
    }
}
