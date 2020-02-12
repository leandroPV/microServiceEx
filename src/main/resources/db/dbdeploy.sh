#!/bin/bash

driver="NADA"
url="NADA"
username="NADA"
password="NADA"

getProperties(){
 driver=${POSTGRE_DRIVER:-org.postgresql.Driver}
 url="jdbc:postgresql:\/\/${DATABASE_HOST:-localhost}:${DATABASE_PORT:-5432}\/api_exemple"
 username=${DATABASE_USER_NAME:-postgres}
 password=${DATABASE_USER_PASSWORD:-postgres}
}


setPomDbDeploy(){
/bin/cp pom-unparsed.xml pom.xml
pom=pom.xml

sed  -i -e  's/\${spring.database.driverClassName}/'"$driver"'/g' $pom
sed  -i -e  's/\${spring.datasource.url}/'"$url"'/g' $pom
sed  -i -e  's/\${spring.datasource.username}/'"$username"'/g' $pom
sed  -i -e  's/\${spring.datasource.password}/'"$password"'/g' $pom
}

runDbDeploy(){
mvn clean test dbdeploy:update
}

debug(){
	echo "driver $driver"
	echo "url $url"
	echo "username $username"
	echo "password  $password"
}

main(){
  environment=$1
  getProperties
  setPomDbDeploy
  runDbDeploy
}
main $*


