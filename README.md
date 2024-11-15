## 프로젝트 설명
- Spring + RabbitMQ(stomp)를 통한 1:1 채팅방 프로젝트
- 주 db는 RDB-H2를 사용하고 채팅 메시지(내역)는 NoSQL-MongoDB을 사용한다
- 메시지에 대한 읽음/안 읽음 기능을 적용하였다
    - 채팅방에 접속 인원을 관리하기 위해 메모리 DB인 Redis를 활용한다
    - 메시지 읽음 기능은 다음과 같은 페이지에서 사용된다
    <div style="display: flex; justify-content: space-between;">
      <img src="https://github.com/user-attachments/assets/7ced4eee-0a35-4ba7-8330-ea245fd864b0" alt="페이지 2" width="30%" />
      <img src="https://github.com/user-attachments/assets/f015f7bd-b475-4860-8db0-178e744628ed" alt="페이지 1" width="30%" />
    </div>
- stomp를 테스트하기 위한 페이지를 지원한다
- 자세한 설명과 테스트는 [블로그](https://lsh2613.tistory.com/260)를 통해 확인해볼 수 있다

## 시작하기
### 1. 도커 rabbitmq 세팅

``` shell
docker pull rabbitmq

docker run -d -p 15672:15672 -p 5672:5672 -p 61613:61613 --name rabbitmq rabbitmq

docker exec rabbitmq rabbitmq-plugins enable rabbitmq_stomp
```

### 2. 도커 컴포즈를 통해 mongodb, redis 띄우기
docker-compose.yml이 존재하는 루트 디렉토리로 이동
``` shell
docker-compose up
```

### 3. 채팅 테스트
1. `localhost:8080/init`을 통해 테스트 데이터(채팅방, 유저) 생성
2. `localhost:8080` 페이지로 접속 (혹은, index.html을 따로 띄우기)
3. Connect를 통해 WebSocket 연결
4. SUB을 통해 큐 구독
5. Send Message를 통해 특정 큐로 메시지 전송
6. 구독된 큐로 메시지가 발행되면 Message를 출력

> 각 텍스트 에디터에 적힌 placeholder로 진행하면 된다
