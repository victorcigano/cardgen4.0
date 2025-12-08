#!/bin/bash
set -e

echo "Starting build process for Render..."

# Make gradlew executable
chmod +x ./gradlew

# Clean and build the project
./gradlew clean build -x test --no-daemon

echo "Build completed successfully!"