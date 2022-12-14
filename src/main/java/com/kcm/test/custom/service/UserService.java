package com.kcm.test.custom.service;

import com.kcm.test.custom.dto.VerifyPasswordResponse;
import com.kcm.test.custom.model.User;
import java.util.Collection;

public interface UserService {

  User getUserById(String userId);

  User getUserByName(String userName);

  User getUserByEmailId(String emailId);

  VerifyPasswordResponse verifyUserPassword(String username, String password);

  int getCount();

  Collection<User> getUsers(String search, Integer firstResult, Integer maxResults);
}
