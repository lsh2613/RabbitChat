#
curl -u pitchain_logstash:pitchain_logstash_password -X GET "localhost:9200/_security/_authenticate?pretty"

# pitchain_logstash 유저 생성 확인
curl -u pitchain_logstash:pitchain_logstash_password http://elasticsearch:9200/

# logstash_writer Role 생성 확인
curl -u elastic:pitchain_elasticsearch_password http://localhost:9200/_security/role/logstash_writer?pretty

# 도커 컴포즈 세팅
docker-compose --profile setup -f docker-compose-elk.yml up --build -d

# es 삭제
docker-compose -f docker-compose-elk.yml down -v # ES 컨테이너와 볼륨(설정) 삭제
rm -rf ./elk/elasticsearch/data                                                                                                                                                                                             ─╯
rm -rf ./elk/elasticsearch/nodes
docker volume rm pitchain_elasticsearchr

docker-compose --env-file /home/ec2-user/app/.env --profile setup -f /home/ec2-user/app/docker-compose-elk.yml up --build -d
docker-compose --profile setup -f docker-compose-elk.yml up --build -d



ssh -i /Users/seungheonlee/Desktop/IntelliJ/pitchain-server-key.pem ec2-user@ec2-43-201-79-150.ap-northeast-2.compute.amazonaws.com