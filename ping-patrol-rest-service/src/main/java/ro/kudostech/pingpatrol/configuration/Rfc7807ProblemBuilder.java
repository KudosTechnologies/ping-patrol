package ro.kudostech.pingpatrol.configuration;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import ro.kudostech.pingpatrol.api.server.model.RFC7807Problem;
import ro.kudostech.pingpatrol.api.server.model.Violation;

@UtilityClass
public class Rfc7807ProblemBuilder {
  public static ResponseEntity<RFC7807Problem> buildErrorResponse(
      Exception exception,
      HttpStatus httpStatus,
      WebRequest request,
      List<Violation> violations) {
    RFC7807Problem problem = new RFC7807Problem();
    problem.setStatus(httpStatus.value());
    problem.setTitle(httpStatus.name());
    problem.setDetail(exception.getMessage());
    problem.setInstance(URI.create(request.getDescription(false)));
    problem.setTimestamp(OffsetDateTime.now());
    problem.setViolations(violations);
    // TODO: add traceId

    return new ResponseEntity<>(problem, httpStatus);
  }
}
