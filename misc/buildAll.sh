#!/bin/bash

searchDir=$PWD

buildFiles=$(find ${searchDir} -name "build.gradle.kts")

echo
echo "Building Example projects"
echo "========================="
echo
for buildFile in ${buildFiles}; do
    project=$(dirname ${buildFile})
    echo "Building $(basename ${project})..."
    echo "-----------------------------------------------"
    cd ${project}
    output=$(./gradlew clean build) && echo "BUILD SUCCESSFUL" || echo "/!\ FAIL!:${output}"
    echo
done

