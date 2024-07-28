


sudo yum install docker -y

sudo yum install commons-compiler-jdk.noarch

sudo yum install maven-local-amazon-corretto21.noarch


curl https://downloads.apache.org/kafka/3.7.0/kafka_2.12-3.7.0.tgz -o kafka_2.12-3.7.0.tgz
tar -xf kafka_2.12-3.7.0.tgz

#was trying to change kafka memory settings. didn't work.
#KAFKA_HEAP_OPTS="-Xms50m -Xmx50m"
#Need an ec2 with enough RAM. 1GB is not enough for everything plus kafka
#failed to map 1,073,741,824 bytes

#Run zookeeper is port 2181
nohup sh bin/zookeeper-server-start.sh config/zookeeper.properties &1

#Run kafka is port 9092
nohup sh bin/kafka-server-start.sh config/server.properties &1

