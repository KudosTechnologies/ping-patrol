package ro.kudostech.pingpatrol.configuration;

import static ro.kudostech.pingpatrol.configuration.Rfc7807ProblemBuilder.buildErrorResponse;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import jakarta.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import ro.kudostech.pingpatrol.api.server.model.RFC7807Problem;
import ro.kudostech.pingpatrol.api.server.model.Violation;
import ro.kudostech.pingpatrol.common.exception.ConstraintViolationHelper;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<RFC7807Problem> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex, WebRequest request) {

    log.error(ex.getMessage(), ex);

    if (ex.getCause() instanceof InvalidFormatException invalidFormatException) {
      invalidFormatException = (InvalidFormatException) ex.getCause();
      final String fieldName =
          invalidFormatException.getPath().stream()
              .findFirst()
              .map(JsonMappingException.Reference::getFieldName)
              .orElse("unknown field");
      final String targetType = invalidFormatException.getTargetType().getSimpleName();
      return invalidField(fieldName, targetType);
    } else if (ex.getCause() instanceof ValueInstantiationException valueInstantiationException) {
      if (valueInstantiationException.getType().getRawClass().isEnum()) {
        return invalidEnum(valueInstantiationException.getType().getRawClass());
      }
      final List<JsonMappingException.Reference> path = valueInstantiationException.getPath();
      final JsonMappingException.Reference reference = path.get(path.size() - 1);
      return invalidField(
          reference.getFieldName(),
          valueInstantiationException.getType().getRawClass().getSimpleName());
    }
    return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request, List.of());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<RFC7807Problem> handleConstraintViolation(
      ConstraintViolationException ex, WebRequest request) {

    final List<Violation> violations =
        ConstraintViolationHelper.createViolations(ex.getConstraintViolations());
    request.setAttribute("violations", violations, RequestAttributes.SCOPE_REQUEST);
    return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request, violations);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<RFC7807Problem> handleAllUncaughtExceptions(
      Exception ex, WebRequest request) {
    log.error(ex.getMessage(), ex);
    return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request, List.of());
  }

  private ResponseEntity<RFC7807Problem> invalidField(String fieldName, String targetType) {
    final RFC7807Problem problem = new RFC7807Problem();
    problem.setStatus(HttpStatus.BAD_REQUEST.value());
    problem.setTitle("Bad Request");
    problem.setDetail(fieldName + " can't be parsed as " + targetType);
    return new ResponseEntity<>(problem, HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<RFC7807Problem> invalidEnum(Class<?> target) {
    final RFC7807Problem problem = new RFC7807Problem();
    problem.setStatus(HttpStatus.BAD_REQUEST.value());
    problem.setTitle("Bad Request");
    problem.setDetail(
        String.format(
            "Invalid %s. Supported values are %s",
            target.getSimpleName(), Arrays.toString(target.getEnumConstants())));
    return new ResponseEntity<>(problem, HttpStatus.BAD_REQUEST);
  }
}
