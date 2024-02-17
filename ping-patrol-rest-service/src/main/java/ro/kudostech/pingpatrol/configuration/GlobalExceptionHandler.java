package ro.kudostech.pingpatrol.configuration;

import static ro.kudostech.pingpatrol.configuration.Rfc7807ProblemBuilder.buildErrorResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<RFC7807Problem> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, WebRequest request) {

    Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();
    for (FieldError error : ex.getFieldErrors()) {
      ConstraintViolation<MethodArgumentNotValidException>
          methodArgumentNotValidExceptionConstraintViolation =
              ConstraintViolationHelper.buildConstraintViolation(
                  error.getDefaultMessage(),
                  MethodArgumentNotValidException.class,
                  "/" + error.getField());
      constraintViolations.add(methodArgumentNotValidExceptionConstraintViolation);
    }
    final List<Violation> violations =
        ConstraintViolationHelper.createViolations(constraintViolations);
    request.setAttribute("violations", violations, RequestAttributes.SCOPE_REQUEST);
    return buildErrorResponse(
        "Method Argument Exception", HttpStatus.BAD_REQUEST, request, violations);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<RFC7807Problem> handleConstraintViolation(
      ConstraintViolationException ex, WebRequest request) {

    final List<Violation> violations =
        ConstraintViolationHelper.createViolations(ex.getConstraintViolations());
    request.setAttribute("violations", violations, RequestAttributes.SCOPE_REQUEST);
    return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request, violations);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<RFC7807Problem> handleAllUncaughtExceptions(
      Exception ex, WebRequest request) {
    log.error(ex.getMessage(), ex);
    return buildErrorResponse(
        ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request, List.of());
  }
}
