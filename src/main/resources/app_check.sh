#!/bin/bash
if [[ $# -lt 3 || $# -gt 4 ]]; then
    echo "Correct usage is : healthcheck http://url_to_check deployed_artifact_id deployed_artifact_version <optional timeout in seconds - default is 120>"
    exit 2
fi

check_url=$1
artifact_id=$2
version=$3
timeout=${4:-120}
count=0
status_url_successfull="Status check URL OK"
artifact_id_successfull="Artifact ID OK"
artifact_version_successfull="Artifact Version OK"

echo "Checking ${check_url} for artifact_id ${artifact_id} with version ${version}"

rm -f /tmp/healthcheck.log
rm -f /tmp/healthcheck_status.log
status_code=0
err_message=""

function is_deployed(){
    
    status_code=0
    http_status=$(curl -v -o /tmp/healthcheck.log -k --connect-timeout 10 -w "\n%{http_code}\n" $check_url  2>/tmp/healthcheck_status.log | tail -1 )
    
            
    if [[ $http_status == "000" ]]; then
            err_message="Couldn't connect to $check_url"
            status_code=2
    elif [[ $http_status == "200" ]]; then
        artifact_check=$(sed -e 's/[{}]/''/g' /tmp/healthcheck.log | awk -v k="text" '{n=split($0,a,","); for (i=1; i<=n; i++) print a[i]}' | grep -i "artifact.id" )
        if [[ "$artifact_check" != *${artifact_id}* ]]; then
            err_message="Artifact deployed to the environment is incorrect. Got ${artifact_check}"
            status_code=2        
        else    
            version_check=$(sed -e 's/[{}]/''/g' /tmp/healthcheck.log | awk -v k="text" '{n=split($0,a,","); for (i=1; i<=n; i++) print a[i]}' | grep -i "artifact.version")
            if [[ "$version_check" != *${version}* ]]; then
                err_message="Artifact version deployed to the environment is incorrect. Got ${version_check}"
                status_code=2
            fi
        fi
    else
        err_message="Got a reponse code of $http_status at $check_url"
        status_code=2
    fi
}


is_deployed
while [[ $status_code != 0 ]]; do
    let count++
    
    sleep 1
    
    if [[ $count == $timeout ]]; then
            echo "$err_message"
            exit 2
    fi
    
    is_deployed
done

printf "${status_url_successfull}\n${artifact_id_successfull}\n${artifact_version_successfull}\n" 
exit 0
