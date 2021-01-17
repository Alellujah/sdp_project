# sdp_project
UAL SDP 2020/2021

docker-compose build and then up to start

To generate a new jar 
./mvnw clean package -DskipTests

if java home is giving an error try

export JAVA_HOME=$(/usr/libexec/java_home)
export PATH=$JAVA_HOME/jre/bin:$PATH


then copy it from target to src/main/docker

cp target/project-0.0.1-SNAPSHOT.jar src/main/docker

thats it