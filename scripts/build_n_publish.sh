#!/bin/bash
set -e

function check_changes() {
    MODULE=$1

    # Check changes in the specified module
    MODULE_CHANGES=$(git diff --name-only HEAD~1 HEAD | { grep "$MODULE/" || true; })

    # Check changes in the scripts folder
    SCRIPTS_CHANGES=$(git diff --name-only HEAD~1 HEAD | { grep "scripts/" || true; })

    echo "Checking changes in $MODULE and scripts folder"

    echo "--> $MODULE folder: $MODULE_CHANGES"
    if [ ! -z "$MODULE_CHANGES" ] || [ ! -z "$SCRIPTS_CHANGES" ]; then
        echo "Changes detected in following files:"
        echo "--> $MODULE folder: $MODULE_CHANGES"
        echo "--> scripts folder: $SCRIPTS_CHANGES"
    else
        echo "No changes detected in $MODULE and scripts folder. Skipping build."
        exit 0
    fi
}

function build_kudconnect_service() {
    cd "$INITIAL_DIR" || exit
    cd kudconnect-service || exit
#    version=$(grep '^version=' gradle.properties | awk -F= '{print $2}')
#    version=$version.$CURRENT_DATETIME
    version="latest"
    echo "Rebuilding image for service: $SERVICE_NAME with version: $version"
    cd "$INITIAL_DIR" || exit
    sudo docker build -t docker-private-kudos-releases.int.hypercloud.ro:80/kudconnect-service:"$version" -f scripts/KudconnectService.Dockerfile .
    docker push docker-private-kudos-releases.int.hypercloud.ro:80/kudconnect-service:$version
    echo "kudconnect-service:$version" >> built_services.txt
}

function build_kudconnect_keycloak() {
    cd "$INITIAL_DIR" || exit
    version="latest"
    sudo docker build -t docker-private-kudos-releases.int.hypercloud.ro:80/kudconnect-keycloak:"$version" -f scripts/KudconnectKeycloak.Dockerfile .
    docker push docker-private-kudos-releases.int.hypercloud.ro:80/kudconnect-keycloak:"$version"
    echo "kudconnect-keycloak:$version" >> built_services.txt
}

function build_kudconnect_web_client() {
    cd "$INITIAL_DIR" || exit
    cd kudconnect-web-client || exit
    version="latest"
#    version=$(jq -r .version package.json)
#    version=$version.$CURRENT_DATETIME
    cd "$INITIAL_DIR" || exit
    echo "Rebuilding image for service: $SERVICE_NAME with version: $version"
    sudo docker build -t docker-private-kudos-releases.int.hypercloud.ro:80/kudconnect-web-client:"$version" -f scripts/KudconnectWebClient.Dockerfile .
    docker push docker-private-kudos-releases.int.hypercloud.ro:80/kudconnect-web-client:"$version"
    echo "kudconnect-web-client:$version" >> built_services.txt
}



# Main part of the script
SERVICE_NAME=$1
CURRENT_DATETIME=$(date +%Y%m%d%H%M)
INITIAL_DIR=$(pwd)

if [ -z "$SERVICE_NAME" ]; then
  echo "No service name provided. Exiting..."
  exit 1
elif [ "$SERVICE_NAME" == "kudconnect-service"  ]; then
  echo "Building service: $SERVICE_NAME"
  check_changes kudconnect-service
  build_kudconnect_service
elif [ "$SERVICE_NAME" == "kudconnect-keycloak" ]; then
  check_changes scripts
  build_kudconnect_keycloak
elif [ "$SERVICE_NAME" == "kudconnect-web-client" ]; then
  check_changes kudconnect-web-client
  build_kudconnect_web_client
else
  echo "Service $SERVICE_NAME is not supported."
  exit 1
fi
