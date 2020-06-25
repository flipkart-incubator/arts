#!/usr/bin/env bash
mvn clean source:jar deploy -DskipTests -DaltDeploymentRepository=flipkart::default::http://artifactory.fkinternal.com/artifactory/v1.0/artifacts/libs-snapshots-local

