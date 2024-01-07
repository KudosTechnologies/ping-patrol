package ro.kudostech.pingpatrol.usermanagement.adapters.input;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ro.kudostech.pingpatrol.api.server.UserProfileApi;
import ro.kudostech.pingpatrol.api.server.model.UpdateUserProfileRequest;
import ro.kudostech.pingpatrol.api.server.model.UserProfile;
import ro.kudostech.pingpatrol.usermanagement.ports.input.UserManagementService;

@RestController
@RequiredArgsConstructor
public class UserManagementController implements UserProfileApi {
  private final UserManagementService userManagementService;

  @Override
  public ResponseEntity<UserProfile> getCurrentUserProfile() {
    UserProfile userDetails = userManagementService.getProfileForLoggedInUser();
    return ResponseEntity.ok(userDetails);
  }

  @Override
  public ResponseEntity<UserProfile> getUserProfileByUserReferenceToken(String userReferenceToken) {
    UserProfile userProfile = userManagementService.getUserProfileByReferenceToken(userReferenceToken);
    return ResponseEntity.ok(userProfile);
  }

  @Override
  public ResponseEntity<Void> updateCurrentUserProfile(
      UpdateUserProfileRequest updateUserProfileRequest) {
    userManagementService.updateProfileForLoggedInUser(updateUserProfileRequest);
    return ResponseEntity.ok().build();
  }


}
