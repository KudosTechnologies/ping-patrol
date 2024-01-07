package ro.kudostech.pingpatrol.applicationcatalog.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.kudostech.pingpatrol.api.server.model.Application;
import ro.kudostech.pingpatrol.api.server.model.ApplicationRedirectUrl;
import ro.kudostech.pingpatrol.api.server.model.RegisterApplicationRequest;
import ro.kudostech.pingpatrol.api.server.model.UpdateApplicationRequest;
import ro.kudostech.pingpatrol.applicationcatalog.adapters.out.persistence.model.ApplicationDbo;
import ro.kudostech.pingpatrol.applicationcatalog.adapters.out.persistence.model.ApplicationRepository;
import ro.kudostech.pingpatrol.applicationcatalog.domain.mapper.ApplicationMapper;
import ro.kudostech.pingpatrol.applicationcatalog.ports.input.ApplicationCatalogService;
import ro.kudostech.pingpatrol.usermanagement.ports.input.UserManagementService;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ApplicationCatalogServiceImpl implements ApplicationCatalogService {
  private final ApplicationMapper applicationMapper;
  private final ApplicationRepository applicationRepository;
  private final UserManagementService userManagementService;

  @Override
  @Transactional
  public Application registerApplication(RegisterApplicationRequest registerApplicationRequest) {
    ApplicationDbo applicationDbo =
        ApplicationDbo.builder()
            .name(registerApplicationRequest.getName())
            .description(registerApplicationRequest.getDescription())
            .baseUrl(registerApplicationRequest.getBaseUrl())
            .authClientId(registerApplicationRequest.getAuthClientId())
            .build();
    applicationRepository.save(applicationDbo);
    return applicationMapper.toApplication(applicationDbo);
  }

  @Override
  @Transactional
  public void updateApplication(
      String applicationId, UpdateApplicationRequest updateApplicationRequest) {
    ApplicationDbo applicationDbo =
        applicationRepository
            .findById(applicationId)
            .orElseThrow(() -> new RuntimeException("Application not found"));
    applicationDbo.setName(updateApplicationRequest.getName());
    applicationDbo.setDescription(updateApplicationRequest.getDescription());
    applicationDbo.setBaseUrl(updateApplicationRequest.getBaseUrl());
    applicationDbo.setAuthClientId(updateApplicationRequest.getAuthClientId());
    applicationRepository.save(applicationDbo);
  }

  @Override
  public List<Application> getAllApplications() {
    return applicationRepository.findAll().stream().map(applicationMapper::toApplication).toList();
  }

  @Override
  public Application getApplication(String applicationId) {
    return applicationMapper.toApplication(
        applicationRepository
            .findById(applicationId)
            .orElseThrow(() -> new RuntimeException("Application not found")));
  }

  @Override
  @Transactional
  public void deleteApplication(String applicationId) {
    applicationRepository.deleteById(applicationId);
  }

  @Override
  public ApplicationRedirectUrl getRedirectionUrl(String applicationId) {
    URI redirectUrl = URI.create(createRedirectionUrl(applicationId));
    return new ApplicationRedirectUrl().redirectUrl(redirectUrl);
  }

  private String createRedirectionUrl(String applicationId) {
    ApplicationDbo applicationDbo = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new RuntimeException("Application not found"));
    String clientId = applicationDbo.getAuthClientId();
    String baseUrl = applicationDbo.getBaseUrl();
    String userId = userManagementService.getProfileForLoggedInUser().getId().toString();
    String token = userManagementService.getUserReferenceToken(userId, clientId);
    return baseUrl + "?user_reference_token=" + token;
  }


}
