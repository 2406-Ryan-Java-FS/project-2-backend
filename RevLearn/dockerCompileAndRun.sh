
set -e

echo "Shell script: Compiling project into jar using maven"
mvn package -DskipTests

#echo "Shell script: generating random secret for JWT"
#RAND=$(openssl rand -base64 512)
##echo "RAND=${RAND}"

#copy this over so dockerfile can access it and copy it into the image
cp ~/application-awstest.properties application-awstest.properties

#echo "Shell script: Building docker image"
docker build --no-cache --build-arg="RAND=${RAND}" -t image1 .

rm application-awstest.properties

echo "Shell script: Running docker image"
docker run -p 8080:8080 image1