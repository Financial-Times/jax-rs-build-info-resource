#!/bin/bash
if [ $# -ne 3 ]; then
    echo "Correct usage is : healthcheck <http://url_to_check> deployed_artifact_id deployed_artifact_version"
    exit 2
fi

check_url=$1
artifact_id=$2
version=$3

echo "Checking ${check_url} for artifact_id ${artifact_id} with version ${version}"


rm -f /tmp/healthcheck.log
rm -f /tmp/healthcheck_status.log


http_status=$(curl -v -o /tmp/healthcheck.log -k --connect-timeout 10 -w "\n%{http_code}\n" $check_url  2>/tmp/healthcheck_status.log | tail -1 )
while [[ $http_status != "200" ]]; do
    sleep 2
    let count++
    if [[ $count == 60 ]]; then
        if [[ $http_status == "000" ]]; then
            echo "ERROR: Couldn't connect to $check_url"
            cat /tmp/healthcheck.log
            exit 2
        elif [[ $http_status != "200" ]]; then
            echo "Startup failed. Could not get a 200 from $check_url after 2 minutes"
            cat /tmp/healthcheck.log
            exit 2
        fi
    fi
    http_status=$(curl -v -o /tmp/healthcheck.log -k --connect-timeout 10 -w "\n%{http_code}\n" $check_url  2>/tmp/healthcheck_status.log | tail -1 )
done

if [[ $http_status == "200" ]]; then
    echo "Health check OK"
fi


artifact_check=$(sed -e 's/[{}]/''/g' /tmp/healthcheck.log | awk -v k="text" '{n=split($0,a,","); for (i=1; i<=n; i++) print a[i]}' | grep -i "artifact.id" )
if [[ "$artifact_check" = *${artifact_id}* ]]; then
    echo "Artifact ID OK"
else
    echo "Artifact deployed to the environment is incorrect. Got ${artifact_check}"
    exit 2
fi

version_check=$(sed -e 's/[{}]/''/g' /tmp/healthcheck.log | awk -v k="text" '{n=split($0,a,","); for (i=1; i<=n; i++) print a[i]}' | grep -i "artifact.version")
if [[ "$version_check" == *${version}* ]]; then
    echo "Artifact Version OK"
else
    echo "Artifact version deployed to the environment is incorrect. Got ${version_check}"
    exit 2
fi
