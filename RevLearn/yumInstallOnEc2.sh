
yum search theThingYouNeed
sudo yum install -y docker
sudo yum install -y commons-compiler-jdk.noarch
sudo yum install -y maven-local-amazon-corretto21.noarch
sudo yum install -y git


sudo yum install nginx.x86_64
cd /etc/nginx/                      #Where yum installed this

sudo systemctl start nginx

#Crucial we create this /etc/nginx/default.d/myLocations.conf
#There will be awful file permissions errors otherwise
sudo nano myLocations.conf
location /project-2-back/ {
        proxy_pass http://localhost:8080/;
}

sudo nginx -t
sudo nginx -T
sudo nginx -s reload

cd /usr/share/nginx/
sudo chown -R ec2-user:ec2-user html/
/usr/share/nginx/html   #default location where static files are served




curl -O https://downloads.apache.org/kafka/3.7.0/kafka_2.12-3.7.0.tgz
tar -xf kafka_2.12-3.7.0.tgz

#was trying to change kafka memory settings. didn't work.
#KAFKA_HEAP_OPTS="-Xms50m -Xmx50m"
#Need an ec2 with enough RAM. 1GB is not enough for everything plus kafka
#failed to map 1,073,741,824 bytes

#Run zookeeper is port 2181
nohup sh bin/zookeeper-server-start.sh config/zookeeper.properties &1

#Run kafka is port 9092
nohup sh bin/kafka-server-start.sh config/server.properties &1

