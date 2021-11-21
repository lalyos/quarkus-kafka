#!/bin/bash -x

# I used this script to create a dev env on an ec2 instance
# mainly to create the docker mage which needs a 
# linux graalvm native binary.
# Unfortuntely the command adviced by docs didnt work on my local env:
#    package -Pnative -Dquarkus.native.container-build=true -Dquarkus.container-image.build=true 

## run as root
## install deps
apt-get update -qq
apt-get install -y \
  build-essential libz-dev zlib1g-dev \
  zip unzip net-tools \
  kafkacat \
  docker-compose

## fix docker for ubuntu
usermod -G docker ubuntu

## install graalvm
curl -L https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-19.3.6/graalvm-ce-java11-linux-amd64-19.3.6.tar.gz|tar -xzC /opt/
cat >> ~/.bash_aliases <<"EOF"
export JAVA_HOME=/opt/graalvm-ce-java11-19.3.6/
export PATH=$JAVA_HOME/bin:$PATH
EOF
. ~/.bash_aliases
gu install native-image

## install maven
apt-get install -y maven

## install redpanda
curl -1sLf 'https://packages.vectorized.io/nzc4ZYQK3WRGd9sy/redpanda/cfg/setup/bash.deb.sh' | sudo -E bash && apt install redpanda

## install kafka
curl "https://downloads.apache.org/kafka/2.6.2/kafka_2.13-2.6.2.tgz" |tar -xzC /opt
echo 'export PATH=$PATH:/opt/kafka_2.13-2.6.2/bin/' >> ~/.bash_aliases
curl -Lo /opt/kafka_2.13-2.6.2/bin/kafka-run-class.sh  https://gist.githubusercontent.com/lalyos/9bc1fb0a63628b7f5984f283eb89d76a/raw/389d0f3d135dbaff0af868ad451f6d43f6361f8a/kafka-run-class.sh
echo 'export BOOTSTRAP_SERVER=127.0.0.1:9092' >> ~/.bash_aliases
curl -Lo /usr/share/bash-completion/completions/kafka https://github.com/lensesio/kafka-autocomplete/releases/download/0.3/kafka

