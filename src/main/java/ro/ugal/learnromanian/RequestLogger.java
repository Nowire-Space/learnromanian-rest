package ro.ugal.learnromanian;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class RequestLogger extends HandlerInterceptorAdapter {
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Value("${learnromanian.log.requests.enabled}")
    private boolean logEnabled;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (logEnabled){
            logger.log(Level.INFO, "REQUEST   {0}", request.getRequestURI());
            Enumeration<?> requestHeaders = request.getHeaderNames();
            while (requestHeaders.hasMoreElements()){
                String key = (String) requestHeaders.nextElement();
                String value = request.getHeader(key);
                if(key.equalsIgnoreCase("authorisation")){
                    byte[] decodeBytes = Base64.getDecoder().decode(value.substring(6));
                    logger.log(Level.INFO, "{0} \t {1}", new Object[] {"initiator",
                        new String(decodeBytes).split(":")[new String(decodeBytes).split(":").length - 2]});
                }else if(key.equalsIgnoreCase("X-Forwarded-For")){
                    logger.log(Level.INFO, "{0} \t {1}", new Object[] {key, value});
                }
            }
            for (String s : response.getHeaderNames()){
                String value = response.getHeader(s);
                if (s.equalsIgnoreCase("Date")){
                    logger.log(Level.INFO, "{0} \t {1}", new Object[] {s,value});
                }
            }
        }
    }
}
