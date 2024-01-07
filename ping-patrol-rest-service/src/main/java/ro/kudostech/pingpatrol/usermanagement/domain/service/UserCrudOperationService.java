package ro.kudostech.pingpatrol.usermanagement.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.kudostech.pingpatrol.api.server.model.UpdateUserProfileRequest;
import ro.kudostech.pingpatrol.api.server.model.UserProfile;
import ro.kudostech.pingpatrol.usermanagement.adapters.output.persistence.UserProfileRepository;
import ro.kudostech.pingpatrol.usermanagement.adapters.output.persistence.model.UserProfileDbo;
import ro.kudostech.pingpatrol.usermanagement.domain.mapper.UserProfileMapper;

@Service
@RequiredArgsConstructor
public class UserCrudOperationService {

  private final UserProfileRepository userProfileRepository;
  private final UserProfileMapper userProfileMapper;

  public UserProfile getUserProfileByEmail(String email) {
    return userProfileRepository
        .findByEmail(email)
        .map(userProfileMapper::toUserProfile)
        .orElseGet(() -> createUserProfile(email));
  }

    public UserProfile getUserProfileById(String userId) {
        return userProfileRepository
            .findById(userId)
            .map(userProfileMapper::toUserProfile)
            .orElseThrow(() -> new RuntimeException("UserDetails not found for user with id " + userId));
    }

  @Transactional
  public void updateUserProfile(String email, UpdateUserProfileRequest userDetails) {
    UserProfileDbo userProfileDbo =
        userProfileRepository
            .findByEmail(email)
            .orElseThrow(
                () -> new RuntimeException("UserDetails not found for user with email " + email));
    userProfileDbo.setAvatar(userDetails.getAvatar());
    userProfileRepository.save(userProfileDbo);
  }

  private UserProfile createUserProfile(String email) {
    UserProfileDbo userProfileDbo = new UserProfileDbo();
    userProfileDbo.setEmail(email);
    userProfileRepository.save(userProfileDbo);
    return userProfileMapper.toUserProfile(userProfileDbo);
  }
}
