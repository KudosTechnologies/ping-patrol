package ro.kudostech.pingpatrol.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import ro.kudostech.pingpatrol.api.client.RFC7807Problem;

@UtilityClass
public class JsonUtils {

  private final ObjectMapper objectMapper = new ObjectMapper();

  public RFC7807Problem parseProblem(String json) throws JsonProcessingException {
    objectMapper.findAndRegisterModules();
    return objectMapper.readValue(json, RFC7807Problem.class);
  }
}
