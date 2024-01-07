package ro.kudostech.pingpatrol.usermanagement.ports.input;

import ro.kudostech.pingpatrol.api.server.model.UpdateUserProfileRequest;
import ro.kudostech.pingpatrol.api.server.model.UserProfile;

public interface UserManagementService {

  /**
   * Gets the user profile by email. If the profile does not exist, it will be created.
   *
   * @return the user profile by email
   */
  UserProfile getProfileForLoggedInUser();

  void updateProfileForLoggedInUser(UpdateUserProfileRequest userDetails);

  /** Gets the user reference token for clientId.
   * If the token does not exist, it will be created.
   * If the token exists, but is expired, a new one will be generated and the old one will be deleted.
   * @param clientId
   * @return the reference token for user
   */
  String getUserReferenceToken(String userId, String clientId);

  /**
   * Gets the user profile by reference token.
   *
   * @param referenceToken the reference token
   * @return the user profile by reference token
   */
  UserProfile getUserProfileByReferenceToken(String referenceToken);
}
