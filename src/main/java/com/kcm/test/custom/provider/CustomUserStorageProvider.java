package com.kcm.test.custom.provider;

import com.kcm.test.custom.dto.VerifyPasswordResponse;
import com.kcm.test.custom.model.User;
import com.kcm.test.custom.service.UserAdapter;
import com.kcm.test.custom.service.UserService;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;

@Slf4j
@RequiredArgsConstructor
public class CustomUserStorageProvider
    implements UserStorageProvider,
        UserLookupProvider,
        CredentialInputValidator,
        UserQueryProvider {

  private final KeycloakSession ksession;
  private final ComponentModel model;
  private final UserService userService;

  @Override
  public boolean supportsCredentialType(String credentialType) {
    return PasswordCredentialModel.TYPE.endsWith(credentialType);
  }

  @Override
  public boolean isConfiguredFor(
      RealmModel realmModel, UserModel userModel, String credentialType) {
    return supportsCredentialType(credentialType);
  }

  @Override
  public boolean isValid(RealmModel realm, UserModel user, CredentialInput credentialInput) {
    log.debug(
        "isValid(realm={},user={},credentialInput.type={})",
        realm.getName(),
        user.getUsername(),
        credentialInput.getType());
    if (!supportsCredentialType(credentialInput.getType())) {
      log.warn("Unsupported credentialInput type");
      return false;
    }
    final VerifyPasswordResponse verifyPasswordResponse =
        userService.verifyUserPassword(user.getUsername(), credentialInput.getChallengeResponse());
    if (verifyPasswordResponse == null) {
      return false;
    }
    return verifyPasswordResponse.isVerified();
  }

  @Override
  public void close() {}

  @Override
  public UserModel getUserById(RealmModel realmModel, String id) {
    // this method is called when we click the username in admin console
    log.debug("getUserById: {}", id);
    UserModel returnValue = null;
    User user = userService.getUserById(StorageId.externalId(id));
    if (user != null) {
      returnValue = toUserModel(user, realmModel);
    }
    return returnValue;
  }

  @Override
  public UserModel getUserByUsername(RealmModel realmModel, String username) {
    // this method is called when try to login with username
    log.debug("getUserByUsername: {}", username);
    UserModel returnValue = null;
    User user = userService.getUserByName(username);
    if (user != null) {
      returnValue = toUserModel(user, realmModel);
    }
    return returnValue;
  }

  @Override
  public UserModel getUserByEmail(RealmModel realmModel, String email) {
    // this method is called when try to login with email
    log.debug("getUserByEmail: {}", email);
    UserModel returnValue = null;
    User user = userService.getUserByEmailId(email);
    if (user != null) {
      returnValue = toUserModel(user, realmModel);
    }
    return returnValue;
  }

  @Override
  public int getUsersCount(RealmModel realm) {
    log.debug("called getUsersCount");
    return userService.getCount();
  }

  @Override
  public Stream<UserModel> searchForUserStream(
      RealmModel realm, String search, Integer firstResult, Integer maxResults) {
    log.debug("searchForUserStream, search={}, first={}, max={}", search, firstResult, maxResults);
    return toUserModelStream(userService.getUsers(search, firstResult, maxResults), realm);
  }

  @Override
  public Stream<UserModel> searchForUserStream(
      RealmModel realm, Map<String, String> params, Integer firstResult, Integer maxResults) {
    log.debug("searchForUserStream, params={}, first={}, max={}", params, firstResult, maxResults);
    return toUserModelStream(userService.getUsers("", firstResult, maxResults), realm);
  }

  @Override
  public Stream<UserModel> getGroupMembersStream(
      RealmModel realm, GroupModel group, Integer firstResult, Integer maxResults) {
    // TODO
    return Stream.empty();
  }

  @Override
  public Stream<UserModel> searchForUserByUserAttributeStream(
      RealmModel realm, String attrName, String attrValue) {
    // TODO
    return Stream.empty();
  }

  private UserModel toUserModel(User user, RealmModel realm) {
    log.debug("Received users[id:{}] from provider", user.getUserId());
    return new UserAdapter(ksession, realm, model, user);
  }

  private Stream<UserModel> toUserModelStream(Collection<User> users, RealmModel realm) {
    log.debug("Received {} users from provider", users.size());
    return users.stream().map(user -> new UserAdapter(ksession, realm, model, user));
  }
}
