package ro.kudostech.pingpatrol.applicationcatalog.adapters.in;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.kudostech.pingpatrol.api.server.ApplicationCatalogApi;
import ro.kudostech.pingpatrol.api.server.model.Application;
import ro.kudostech.pingpatrol.api.server.model.ApplicationRedirectUrl;
import ro.kudostech.pingpatrol.api.server.model.RegisterApplicationRequest;
import ro.kudostech.pingpatrol.api.server.model.ResourceUUID;
import ro.kudostech.pingpatrol.api.server.model.UpdateApplicationRequest;
import ro.kudostech.pingpatrol.applicationcatalog.ports.input.ApplicationCatalogService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ApplicationCatalogController implements ApplicationCatalogApi {

  private final ApplicationCatalogService applicationCatalogService;

  @Override
  @PreAuthorize("hasRole('admin')")
  public ResponseEntity<ResourceUUID> registerApplication(
      RegisterApplicationRequest registerApplicationRequest) {
    Application application =
        applicationCatalogService.registerApplication(registerApplicationRequest);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(application.getId())
            .toUri();
    return ResponseEntity.created(location).body(new ResourceUUID().id(application.getId()));
  }

  @Override
  @PreAuthorize("hasRole('admin')")
  public ResponseEntity<Void> updateApplication(
      UUID applicationId, UpdateApplicationRequest updateApplicationRequest) {
    applicationCatalogService.updateApplication(applicationId.toString(), updateApplicationRequest);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<List<Application>> getAllApplications() {
    return ResponseEntity.ok(applicationCatalogService.getAllApplications());
  }

  @Override
  public ResponseEntity<Application> getApplicationById(UUID applicationId) {
    Application application = applicationCatalogService.getApplication(applicationId.toString());
    return ResponseEntity.ok(application);
  }

  @Override
  public ResponseEntity<Void> deleteApplication(UUID applicationId) {
    applicationCatalogService.deleteApplication(applicationId.toString());
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<ApplicationRedirectUrl> getRedirectionUrl(UUID applicationId) {
    ApplicationRedirectUrl applicationRedirectUrl =
        applicationCatalogService.getRedirectionUrl(applicationId.toString());
    return ResponseEntity.ok(applicationRedirectUrl);
  }
}
