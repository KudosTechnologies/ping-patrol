package ro.kudostech.pingpatrol.common;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Slf4j
@Mapper(componentModel = "spring")
public abstract class CommonMapper {

  @Named("stringToUUID")
  public UUID stringToUUID(String id) {
    return id == null ? null : UUID.fromString(id);
  }

  @Named("UUIDToString")
  public String uuidToString(UUID id) {
    return id == null ? null : id.toString();
  }

  @Named("stringToURI")
  public URI stringToURI(String url) {
    try {
      return url == null ? null : new URI(url);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Invalid URL", e);
    }
  }

  @Named("URIToString")
  public String uriToString(URI url) {
    return url == null ? null : url.toString();
  }
}
