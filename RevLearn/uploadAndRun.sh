
set -e

echo "AWS_EC2_IP=$AWS_EC2_IP"

echo "Shell script: Uploading src code files"
sftp -i ~/project22.pem ec2-user@$AWS_EC2_IP << 'END'
  put pom.xml
  put -r src
END

#echo "Shell script: Uploading jar file"
#sftp -i ~/project22.pem ec2-user@$AWS_EC2_IP << 'END'
#  put target/RevLearn-0.0.1-SNAPSHOT.jar
#END

ssh -i ~/project22.pem ec2-user@$AWS_EC2_IP << 'END'
  echo "Shell script: Compiling using maven"
  mvn package -DskipTests
END


ssh -i ~/project22.pem ec2-user@$AWS_EC2_IP << 'END'
  echo "Shell script: Killing the running jar"
  sudo pkill -f RevLearn-0.0.1-SNAPSHOT.jar
END

echo "Shell script: Waiting for the jar to stop"
sleep 6

ssh -i ~/project22.pem ec2-user@$AWS_EC2_IP << 'END'
  echo "Shell script: Starting the new jar"
  sudo java -jar target/RevLearn-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2 --server.port=8080
END

#sudo nohup java -jar target/RevLearn-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2 --server.port=80 > theOutput.txt 2>&1 &