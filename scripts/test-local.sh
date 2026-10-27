#!/bin/bash

echo "==========================="
echo "BUILDING IoTScanner 	"
echo "==========================="

mvn clean package -DskipTests


if [ $? -ne 0 ]; then
    echo "Build failed."
    exit 1
fi
echo "Copying dependencies..."

mvn dependency:copy-dependencies \
    -DincludeScope=runtime \
    -DoutputDirectory=target/dependency

if [ $? -ne 0 ]; then
    echo "Dependency copy failed."
    exit 1
fi

echo "Running Tests...."

mvn test

echo "Tests Completed..."
