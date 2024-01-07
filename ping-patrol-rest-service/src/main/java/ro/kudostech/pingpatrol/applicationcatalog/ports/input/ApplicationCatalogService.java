package ro.kudostech.pingpatrol.applicationcatalog.ports.input;

import ro.kudostech.pingpatrol.api.server.model.Application;
import ro.kudostech.pingpatrol.api.server.model.ApplicationRedirectUrl;
import ro.kudostech.pingpatrol.api.server.model.RegisterApplicationRequest;
import ro.kudostech.pingpatrol.api.server.model.UpdateApplicationRequest;

import java.util.List;

public interface ApplicationCatalogService {
  Application registerApplication(RegisterApplicationRequest registerApplicationRequest);

  void updateApplication(String applicationId, UpdateApplicationRequest updateApplicationRequest);

  List<Application> getAllApplications();

  Application getApplication(String applicationId);

  void deleteApplication(String applicationId);

  ApplicationRedirectUrl getRedirectionUrl(String applicationId);
}
