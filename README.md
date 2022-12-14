# keycloak-custom-storage-provider
Sample custom user storage provider for keycloak

## Setup keycloak server locally

### Prerequisites
1. java 11
2. keycloak server(see the installation steps below)

### Build the application
```shell
mvn clean install
```

## Installing the custom provider in keycloak
```shell
cp ./target/keycloak-custom-storage-provider-*.jar ${KEYCLOAK_HOME}/providers/ 
```

## Installing keycloak server
1. Download latest http://www.keycloak.org/downloads.html
2. unzip keycloak-20.0.1.zip
3. cd keycloak-20.0.1/bin
4. start the keycloak server
   ```
    ./kc.sh start-dev
    or
    ./kc.sh start-dev --http-port=8083
    or
    ./kc.sh start-dev --http-port=8083 --log-level=DEBUG
    ```

* for downloading old versions: https://github.com/keycloak/keycloak/releases/download/19.0.1/keycloak-19.0.1.zip




