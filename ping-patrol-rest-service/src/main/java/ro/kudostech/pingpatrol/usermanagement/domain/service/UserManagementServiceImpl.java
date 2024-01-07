package ro.kudostech.pingpatrol.usermanagement.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ro.kudostech.pingpatrol.api.server.model.UpdateUserProfileRequest;
import ro.kudostech.pingpatrol.api.server.model.UserProfile;
import ro.kudostech.pingpatrol.common.auth.TokenBasedAccessUtils;
import ro.kudostech.pingpatrol.usermanagement.ports.input.UserManagementService;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

  private final UserCrudOperationService userCrudOperationService;
  private final ReferenceTokenService referenceTokenService;

  @Override
  public UserProfile getProfileForLoggedInUser() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    return userCrudOperationService.getUserProfileByEmail(email);
  }

  @Override
  @Transactional
  public void updateProfileForLoggedInUser(UpdateUserProfileRequest userDetails) {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    userCrudOperationService.updateUserProfile(email, userDetails);
  }

  @Override
  public String getUserReferenceToken(String userId, String clientId) {
    return referenceTokenService.getUserReferenceTokenForClient(userId, clientId);
  }

  @Override
  public UserProfile getUserProfileByReferenceToken(String referenceToken) {
    String clientId =
        TokenBasedAccessUtils.getClientId(SecurityContextHolder.getContext().getAuthentication());
    String userId = referenceTokenService.getUserIdByReferenceToken(referenceToken, clientId);
    return userCrudOperationService.getUserProfileById(userId);
  }
}
