package nowire.space.learnromanian.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {
    private static final Map<Class, HttpStatus> STATUS_MAP = Map.of( MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST,
            IllegalArgumentException.class, HttpStatus.BAD_REQUEST,
            NoSuchElementException.class, HttpStatus.NOT_FOUND);

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleException(Exception ex) throws Exception{
        HttpStatus httpCode = STATUS_MAP.get(ex.getClass());
        if (httpCode == null)
            throw ex;
        Map<String, String> error = new TreeMap<>();
        error.put("error-type", ex.getLocalizedMessage());
        error.put("error-message", httpCode.getReasonPhrase());
        return ResponseEntity.status(httpCode).body(error);
    }

}
