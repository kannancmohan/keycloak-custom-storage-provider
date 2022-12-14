package com.kcm.test.custom.provider;

import com.kcm.test.custom.service.SampleUserService;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

public class CustomUserStorageProviderFactory
    implements UserStorageProviderFactory<CustomUserStorageProvider> {

  public static final String PROVIDER_ID = "custom-user-provider";

  @Override
  public CustomUserStorageProvider create(
      KeycloakSession keycloakSession, ComponentModel componentModel) {
    return new CustomUserStorageProvider(keycloakSession, componentModel, new SampleUserService());
  }

  @Override
  public String getId() {
    return PROVIDER_ID;
  }

  @Override
  public String getHelpText() {
    return "Custom User Provider";
  }
}
