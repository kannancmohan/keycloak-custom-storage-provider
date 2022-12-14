package com.kcm.test.custom.service;

import com.kcm.test.custom.dto.VerifyPasswordResponse;
import com.kcm.test.custom.model.User;
import java.util.Collection;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SampleUserService implements UserService {
  private static final Map<String, User> SAMPLE_USERS =
      Map.of(
          "usr00001",
              new User("Kannan", "Mohan", "kanna.mohan@example.com", "kanna.mohan", "usr00001"),
          "usr00002", new User("John", "Tom", "john.tom@example.com", "john.tom", "usr00002"));

  @Override
  public User getUserById(String userId) {
    return SAMPLE_USERS.get(userId);
  }

  @Override
  public User getUserByName(String userName) {
    return SAMPLE_USERS.values().stream()
        .filter(user -> userName.equals(user.getUserName()))
        .findFirst()
        .orElse(null);
  }

  @Override
  public User getUserByEmailId(String emailId) {
    return SAMPLE_USERS.values().stream()
        .filter(user -> emailId.equals(user.getEmail()))
        .findFirst()
        .orElse(null);
  }

  @Override
  public VerifyPasswordResponse verifyUserPassword(String username, String password) {
    log.debug("verifyUserPassword for username:{}", username);
    // TODO do actual password check
    return VerifyPasswordResponse.of(true);
  }

  @Override
  public int getCount() {
    return SAMPLE_USERS.size();
  }

  @Override
  public Collection<User> getUsers(String search, Integer firstResult, Integer maxResults) {
    return SAMPLE_USERS.values();
  }
}
